package com.lvbby.codema.core;

import com.lvbby.codema.core.bean.CodemaBeanFactory;
import com.lvbby.codema.core.bean.DefaultCodemaBeanFactory;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.config.NotBlank;
import com.lvbby.codema.core.source.SourceLoader;
import com.lvbby.codema.core.utils.ReflectionUtils;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

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
        Class codemaMachineClass = findCodemaMachineClass(config.getClass());
        CodemaMachine instance = (CodemaMachine) ReflectionUtils.instance(codemaMachineClass);
        codemaContext.getRunMap().put(instance, config);
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
                CommonCodemaConfig config = codemaContext.getRunMap().get(codemaMachine);
                //初始化config
                config.init();
                codemaMachine.code(codemaContext, config);
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
