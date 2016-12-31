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
    /**
     * 项目的入参，由Common的from字段解析而来，所有模块都以这个参数为入参
     */
    private Object source;
    private Codema codema;

    Map<Class, Object> paramMap = Maps.newConcurrentMap();

    public Codema getCodema() {
        return codema;
    }

    public void setCodema(Codema codema) {
        this.codema = codema;
    }

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
        return Optional.ofNullable((T) configLoader.getConfig(clz));
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

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}
