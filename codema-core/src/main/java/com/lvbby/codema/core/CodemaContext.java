package com.lvbby.codema.core;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Optional;

/**
 * Created by lipeng on 16/12/23.
 */
public class CodemaContext {
    /**
     * 配置
     */
    private ConfigLoader configLoader;

    Map<Class, Object> paramMap = Maps.newConcurrentMap();

    public <T> Optional<T> getParam(Class<T> clz) {
        return Optional.ofNullable((T) paramMap.get(clz));
    }

    public void storeParam(Object result) {
        paramMap.put(result.getClass(), result);
    }

    public <T> T getConfig(Class<T> clz) {
        return configLoader.getConfig(clz);
    }

    public <T> Optional<T> loadConfig(Class<T> clz) {
        return java.util.Optional.ofNullable((T) configLoader.getConfig(clz));
    }

    public boolean hasConfig(Class clz) {
        return getConfig(clz) != null;
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    public void setConfigLoader(ConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

}
