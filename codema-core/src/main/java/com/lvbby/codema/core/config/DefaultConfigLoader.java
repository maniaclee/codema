package com.lvbby.codema.core.config;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.Validate;

import java.util.Map;

/**
 * Created by dushang.lp on 2017/5/14.
 */
public class DefaultConfigLoader implements ConfigLoader {
    private Map<Class, Object> configs = Maps.newHashMap();

    public <T extends CommonCodemaConfig> DefaultConfigLoader addConfig(T config) {
        Validate.isTrue(config != null, "clz can't be null");
        Validate.isTrue(configs.get(config.getClass()) == null, String.format("%s config already exists", config.getClass().getName()));
        configs.put(config.getClass(), config);
        return this;
    }

    public DefaultConfigLoader addConfig(Class clz, Object config) {
        Validate.isTrue(clz != null && config != null, "clz or config can't be null");
        Validate.isTrue(clz.isAssignableFrom(config.getClass()), String.format("%s is not instance of %s", config.getClass().getName(), clz.getName()));
        configs.put(clz, config);
        return this;
    }

    /***
     * 把config类的所有父类也都加入
     * @param config
     * @param <T>
     * @return
     */
    public <T extends CommonCodemaConfig> DefaultConfigLoader addConfigRecursive(T config) {
        Validate.isTrue(config != null, "clz can't be null");
        Validate.isTrue(configs.get(config.getClass()) == null, String.format("%s config already exists", config.getClass().getName()));
        for (Class<?> clz = config.getClass(); clz != null && CommonCodemaConfig.class.isAssignableFrom(clz); clz = clz.getSuperclass()) {
            configs.put(clz, config);
        }
        return this;
    }


    @Override
    public <T> T getConfig(Class<T> clz) {
        return (T) configs.get(clz);
    }
}
