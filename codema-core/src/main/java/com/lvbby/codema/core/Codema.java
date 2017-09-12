package com.lvbby.codema.core;

import com.lvbby.codema.core.config.*;
import com.lvbby.codema.core.source.*;
import com.lvbby.codema.core.utils.*;
import java.lang.reflect.*;
import org.apache.commons.lang3.*;

/**
 * Created by lipeng on 16/12/23.
 */
public class Codema {
    private CodemaContext codemaContext = new CodemaContext();

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
        codemaContext.getRunMap().put(configBinder, config);
        return this;
    }

    public <T extends CommonCodemaConfig> Codema bind(T config) {
        CodemaMachine instance = config.loadCodemaMachine();
        Validate.notNull(instance, "no codema machine found for config %s", config.getClass().getSimpleName());
        codemaContext.getRunMap().put(instance, config);
        return this;
    }

    public Codema withSource(Object object) {
        codemaContext.setSource(object);
        return this;
    }

    public Codema source(SourceLoader sourceLoader) throws Exception {
        withSource(sourceLoader.loadSource());
         return this;
    }

    public void run() throws Exception {
        CodemaContextHolder.setCodemaContext(codemaContext);
        try {
            Validate.notNull(codemaContext.getSource(),"no source found");
            checkConfig();
            /** 执行 */
            for (CodemaMachine codemaMachine : codemaContext.getRunMap().keySet()) {
                for (CommonCodemaConfig config : codemaContext.getRunMap().get(codemaMachine)) {
                    //初始化config
                    config.init();
                    try {
                        codemaMachine.code(codemaContext, config);
                    } catch (Error e) {
                        throw new RuntimeException(String.format("error machine[%s], error[%s]", codemaMachine.getClass().getName(),e.getMessage()),e);
                    }
                }
            }
        } finally {
            CodemaContextHolder.clear();
        }
    }
    private void checkConfig() throws Exception {
        for (CommonCodemaConfig commonCodemaConfig : codemaContext.getRunMap().values()) {
            //NotBlank
            for (Field field : ReflectionUtils.getAllFields(commonCodemaConfig.getClass(),
                    field -> field.isAnnotationPresent(NotBlank.class))) {
                field.setAccessible(true);
                Object o = field.get(commonCodemaConfig);
                if (o instanceof String) {
                    Validate.notBlank(o.toString(), "%s.%s can't be blank");
                }
            }
        }
    }

}
