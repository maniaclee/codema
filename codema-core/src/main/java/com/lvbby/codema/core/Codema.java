package com.lvbby.codema.core;

import com.lvbby.codema.core.bean.CodemaBeanFactory;
import com.lvbby.codema.core.bean.DefaultCodemaBeanFactory;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.source.SourceLoader;
import com.lvbby.codema.core.utils.ReflectionUtils;
import org.apache.commons.lang3.Validate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 16/12/23.
 */
public class Codema {
    private CodemaBeanFactory codemaBeanFactory = new DefaultCodemaBeanFactory();

    private LinkedHashMap<CodemaMachine, CommonCodemaConfig> runMap = new LinkedHashMap<>();
    private CodemaContext codemaContext = new CodemaContext();

    {
        codemaContext.setCodema(this);
    }


    public static <T extends CommonCodemaConfig> void exec(T config) throws Exception {
        new Codema().bind(config).run();
    }

    public static <T extends CommonCodemaConfig> void exec(T config, SourceLoader... sourceLoaders) throws Exception {
        Codema codema = new Codema().bind(config);
        if (sourceLoaders != null) {
            for (SourceLoader sourceLoader : sourceLoaders) {
                codema.source(sourceLoader);
            }
        }
        codema.run();
    }

    /***
     *
     * @param configBinder
     * @param config
     * @param <T>
     * @return
     */
    public <T extends CommonCodemaConfig> Codema bind(CodemaMachine<T> configBinder, T config) {
        runMap.put(configBinder, config);
        return this;
    }

    public <T extends CommonCodemaConfig> Codema bind(T config) {
        Class codemaMachineClass = findCodemaMachineClass(config.getClass());
        CodemaMachine instance = (CodemaMachine) ReflectionUtils.instance(codemaMachineClass);
        runMap.put(instance, config);
        return this;
    }

    private Class<?> findCodemaMachineClass(Class<? extends CommonCodemaConfig> configClz) {
        ConfigBind annotation = configClz.getAnnotation(ConfigBind.class);
        if (annotation == null || annotation.value() == null) {
            throw new RuntimeException(String.format("no ConfigBind found for config %s",
                    configClz.getName()));
        }
        Class<?> value = annotation.value();
        if (!CodemaMachine.class.isAssignableFrom(value)) {
            throw new RuntimeException(
                    String.format("config must bind a %s", CodemaMachine.class.getName()));
        }
        return value;
    }

    public Codema withSource(Object object) {
        codemaContext.addSource(object);
        return this;
    }

    public Codema source(SourceLoader sourceLoader) throws Exception {
        return withSource(sourceLoader.loadSource());
    }

    public void run() throws Exception {
        Validate.notEmpty(codemaContext.getSourceMap(), "no source found");
        /** 执行 */
        for (CodemaMachine codemaMachine : runMap.keySet()) {
            CommonCodemaConfig config = runMap.get(codemaMachine);
            //初始化config
            config.init();
            codemaMachine.code(codemaContext, config);
        }
    }

    public <T extends CommonCodemaConfig> T findConfig(Class<T> clz) {
        return (T) findConfigBlur(clz);
    }

    public Object findConfigBlur(Class clz) {
        List<CommonCodemaConfig> configs = runMap.values().stream().filter(commonCodemaConfig -> clz.equals(commonCodemaConfig.getClass())).collect(Collectors.toList());
        if (configs.size() > 1)
            throw new IllegalArgumentException(String.format("multi config found : %s", clz.getName()));
        return configs.isEmpty() ? null : configs.get(0);
    }

    public CodemaBeanFactory getCodemaBeanFactory() {
        return codemaBeanFactory;
    }

    public void setCodemaBeanFactory(CodemaBeanFactory codemaBeanFactory) {
        this.codemaBeanFactory = codemaBeanFactory;
    }


}
