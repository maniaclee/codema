package com.lvbby.codema.core;

import com.google.common.collect.Maps;
import com.lvbby.codema.core.bean.CodemaBeanFactory;
import com.lvbby.codema.core.config.ConfigLoader;

import java.util.List;
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
     * 项目的入参, 把source按type分类
     */
    private Map<Class, Object> sourceMap = Maps.newHashMap();
    private Codema codema;

    Map<Class, Object> paramMap = Maps.newConcurrentMap();

    public <T> T getSourceByType(Class<T> clz) {
        return (T) sourceMap.get(clz);
    }

    public void addSource(Object source) {
        sourceMap.put(source.getClass(), source);
    }

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

}
