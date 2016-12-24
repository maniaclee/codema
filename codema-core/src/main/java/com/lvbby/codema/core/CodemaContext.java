package com.lvbby.codema.core;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by lipeng on 16/12/23.
 */
public class CodemaContext {
    /**
     * 配置
     */
    private ConfigLoader configLoader;

    /**
     * 结果，不同模块之间的结果可以互相共享
     */
    Map<Class, Object> resultMap = Maps.newConcurrentMap();

    public <T> T getResult(Class<T> clz) {
        return (T) resultMap.get(clz);
    }

    public void storeResult(Object result) {
        resultMap.put(result.getClass(), result);
    }

    public <T> T getConfig(Class<T> clz) {
        return configLoader.getConfig(clz);
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
