package com.lvbby.codema.core;

import com.lvbby.codema.core.bean.CodemaBeanFactory;
import com.lvbby.codema.core.bean.DefaultCodemaBeanFactory;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.utils.ReflectionUtils;

import java.util.LinkedHashMap;

/**
 * Created by lipeng on 16/12/23.
 */
public class Codema {
    private CodemaBeanFactory codemaBeanFactory = new DefaultCodemaBeanFactory();

    private LinkedHashMap<CodemaMachine, CommonCodemaConfig> runMap = new LinkedHashMap<>();
    CodemaContext codemaContext = new CodemaContext();

    {
        codemaContext.setCodema(this);
    }

    public static <T extends CommonCodemaConfig> void exec(T config) throws Exception {
        new Codema().bind(config).run();
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
        ConfigBind annotation = config.getClass().getAnnotation(ConfigBind.class);
        if (annotation == null || annotation.value() == null) {
            throw new RuntimeException(String.format("no ConfigBind found for config %s",
                    config.getClass().getName()));
        }
        Class<?> value = annotation.value();
        if (!CodemaMachine.class.isAssignableFrom(value)) {
            throw new RuntimeException(
                    String.format("config must bind a %s", CodemaMachine.class.getName()));
        }
        runMap.put((CodemaMachine) ReflectionUtils.instance(value), config);
        return this;
    }

    public Codema withSource(Object object) {
        codemaContext.addSource(object);
        return this;
    }

    public void run() throws Exception {
        /** 整个codema生命周期内共用一个context */
        //        codemaContext.setConfigLoader(configLoader);

        /** 执行 */
        for (CodemaMachine codemaMachine : runMap.keySet()) {
            codemaMachine.code(codemaContext, runMap.get(codemaMachine));
        }
    }

    public CodemaBeanFactory getCodemaBeanFactory() {
        return codemaBeanFactory;
    }

    public void setCodemaBeanFactory(CodemaBeanFactory codemaBeanFactory) {
        this.codemaBeanFactory = codemaBeanFactory;
    }
}
