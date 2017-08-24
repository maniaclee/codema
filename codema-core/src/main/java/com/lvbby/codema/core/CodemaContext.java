package com.lvbby.codema.core;

import com.google.common.collect.Maps;
import com.lvbby.codema.core.config.CommonCodemaConfig;

import java.util.Map;
import java.util.Optional;

/**
 * Created by lipeng on 16/12/23.
 */
public class CodemaContext {
    /**
     * 项目的入参, 把source按type分类
     */
    private Map<Class, Object> sourceMap = Maps.newHashMap();
    private Codema codema;

    Map<Class, Object> paramMap = Maps.newConcurrentMap();

    public Map<Class, Object> getSourceMap() {
        return sourceMap;
    }

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

    public <T extends CommonCodemaConfig> T getConfig(Class<T> clz) {
        return codema.findConfig(clz);
    }

}
