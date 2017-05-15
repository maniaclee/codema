package com.lvbby.codema.core;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.bean.CodemaBeanFactory;
import com.lvbby.codema.core.bean.DefaultCodemaBeanFactory;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.config.ConfigLoader;
import com.lvbby.codema.core.config.YamlConfigLoader;
import com.lvbby.codema.core.error.CodemaException;
import com.lvbby.codema.core.inject.CodemaInject;
import com.lvbby.codema.core.inject.CodemaInjectable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.net.URI;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 16/12/23.
 */
public class Codema {
    private ConfigLoader configLoader;
    private List<CodemaMachine> codemaMachines;
    private SourceParserFactory sourceParserFactory;
    private CodemaInject codemaInject = new CodemaInject();
    private CodemaBeanFactory codemaBeanFactory = new DefaultCodemaBeanFactory();
    private ClassLoader classLoader;

    public static Codema fromYaml(String yaml) throws Exception {
        return from(new YamlConfigLoader().load(yaml));
    }


    public static Codema from(ConfigLoader configLoader) {
        return new Codema(configLoader);
    }

    private Codema(ConfigLoader configLoader) {
        this.configLoader = configLoader;
        init();
    }

    private void init() {
        //加载CodeMachine
        this.codemaMachines = loadService(CodemaMachine.class, classLoader);
        this.sourceParserFactory = SourceParserFactory.of(loadService(SourceParser.class, classLoader));
        this.codemaMachines.addAll(loadService(CodemaInjectable.class).stream().map(codemaInjectable -> codemaInject.toCodemaMachine(codemaInjectable)).flatMap(r -> r.stream()).collect(Collectors.toList()));
    }

    public Codema setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    public void run() throws Exception {
        /** 整个codema生命周期内共用一个context */
        CodemaContext codemaContext = new CodemaContext();
        codemaContext.setConfigLoader(configLoader);
        codemaContext.setCodema(this);

        CommonCodemaConfig config = codemaContext.getConfig(CommonCodemaConfig.class);
        Validate.notNull(config, "common config is missing");

        /** 解析输入，注入到context里 */
        Object source = findSourceParser(config.getFrom()).parse(URI.create(config.getFrom()));
        codemaContext.setSource(source);

        /** 设置上下文holder */
        CodemaContextHolder.setCodemaContext(codemaContext);
        /** 执行 */
        try {
            for (CodemaMachine codemaMachine : codemaMachines) {
                //没有找到配置的不执行，filter
                if (codemaContext.getConfig(codemaMachine.getConfigType()) != null)
                    codemaMachine.code(codemaContext);
            }
        } finally {
            CodemaContextHolder.clear();
        }
    }

    /***
     *  查找id以pack开头的beans
     */
    public <T> List<T> findBeans(String pack, Class<T> clz) {
        return getCodemaBeanFactory().getBeans(codemaBean -> StringUtils.isBlank(pack) || codemaBean.getId().startsWith(pack), clz);
    }

    private SourceParser findSourceParser(String from) throws CodemaException {
        SourceParser load = sourceParserFactory.load(from);
        if (load == null)
            throw new CodemaException(String.format("can't find source parser for %s", from));
        return load;
    }

    public Object parseSource(String url) throws Exception {
        return findSourceParser(url).parse(URI.create(url));
    }

    public Codema addCodemaMachine(CodemaMachine codemaMachine) {
        this.codemaMachines.add(codemaMachine);
        return this;
    }

    public Codema addCodemaMachineInject(Object codemaMachine) {
        this.codemaMachines.addAll(codemaInject.toCodemaMachine(codemaMachine));
        return this;
    }

    public <T extends CodemaMachine> T getCodemaMachineByType(Class<T> clz) {
        return (T) codemaMachines.stream().filter(codemaMachine -> codemaMachine.getClass().equals(clz));
    }

    public Codema addCodemaMachine(SourceParser sourceParser) {
        this.sourceParserFactory.getSourceParsers().add(sourceParser);
        return this;
    }


    private <T> List<T> loadService(Class<T> clz) {
        return Lists.newArrayList(ServiceLoader.load(clz, Thread.currentThread().getContextClassLoader()));
    }

    private <T> List<T> loadService(Class<T> clz, ClassLoader classLoader) {
        return Lists.newArrayList(ServiceLoader.load(clz, classLoader == null ? Thread.currentThread().getContextClassLoader() : classLoader));
    }

    public CodemaInject getCodemaInject() {
        return codemaInject;
    }

    public CodemaBeanFactory getCodemaBeanFactory() {
        return codemaBeanFactory;
    }
}
