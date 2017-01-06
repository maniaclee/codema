package com.lvbby.codema.core;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.config.ConfigLoader;
import com.lvbby.codema.core.config.YamlConfigLoader;
import com.lvbby.codema.core.error.CodemaException;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaInject;
import com.lvbby.codema.core.bean.DefaultCodemaBeanFactory;
import com.lvbby.codema.core.bean.CodemaBeanFactory;
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

    public static Codema fromYaml(String yaml) throws Exception {
        ConfigLoader configLoader = new YamlConfigLoader();
        configLoader.load(yaml);
        return new Codema(configLoader);
    }

    public Codema(ConfigLoader configLoader) {
        this.configLoader = configLoader;
        //加载CodeMachine
        this.codemaMachines = loadService(CodemaMachine.class);
        this.sourceParserFactory = SourceParserFactory.of(loadService(SourceParser.class));
        this.codemaMachines.addAll(loadService(CodemaInjectable.class).stream().map(codemaInjectable -> codemaInject.toCodemaMachine(codemaInjectable)).flatMap(r -> r.stream()).collect(Collectors.toList()));
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

        /** 执行 */
        for (CodemaMachine codemaMachine : codemaMachines) {
            //没有找到配置的不执行，filter
            if (codemaContext.getConfig(codemaMachine.getConfigType()) != null)
                codemaMachine.code(codemaContext);
        }
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
        return Lists.newArrayList(ServiceLoader.load(clz));
    }

    public CodemaInject getCodemaInject() {
        return codemaInject;
    }

    public CodemaBeanFactory getCodemaBeanFactory() {
        return codemaBeanFactory;
    }
}
