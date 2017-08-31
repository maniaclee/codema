package com.lvbby.codema.core;

import com.google.common.collect.Maps;
import com.lvbby.codema.core.bean.CodemaBeanFactory;
import com.lvbby.codema.core.bean.DefaultCodemaBeanFactory;
import com.lvbby.codema.core.config.CommonCodemaConfig;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 16/12/23.
 */
public class CodemaContext {
    /**
     * 项目的入参, 把source按type分类
     */
    private Map<Class, Object> sourceMap = Maps.newHashMap();
    private Object source;
    private CodemaBeanFactory                                codemaBeanFactory = new DefaultCodemaBeanFactory();
    private LinkedHashMap<CodemaMachine, CommonCodemaConfig> runMap            = new LinkedHashMap<>();


    Map<Class, Object> paramMap = Maps.newConcurrentMap();


    public <T extends CommonCodemaConfig> T findConfig(Class<T> clz) {
        return (T) findConfigBlur(clz);
    }

    public Object findConfigBlur(Class clz) {
        List<CommonCodemaConfig> configs = runMap.values().stream().filter(commonCodemaConfig -> clz.equals(commonCodemaConfig.getClass())).collect(
                Collectors.toList());
        if (configs.size() > 1)
            throw new IllegalArgumentException(String.format("multi config found : %s", clz.getName()));
        return configs.isEmpty() ? null : configs.get(0);
    }
    @Deprecated
    public <T> T getSourceByType(Class<T> clz) {
        return (T) sourceMap.get(clz);
    }

    public <T> Optional<T> getParam(Class<T> clz) {
        return Optional.ofNullable((T) paramMap.get(clz));
    }

    public void storeParam(Object result) {
        paramMap.put(result.getClass(), result);
    }

    public <T extends CommonCodemaConfig> T getConfig(Class<T> clz) {
        return findConfig(clz);
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public CodemaBeanFactory getCodemaBeanFactory() {
        return codemaBeanFactory;
    }

    public void setCodemaBeanFactory(CodemaBeanFactory codemaBeanFactory) {
        this.codemaBeanFactory = codemaBeanFactory;
    }

    public LinkedHashMap<CodemaMachine, CommonCodemaConfig> getRunMap() {
        return runMap;
    }

    public void setRunMap(LinkedHashMap<CodemaMachine, CommonCodemaConfig> runMap) {
        this.runMap = runMap;
    }
}
