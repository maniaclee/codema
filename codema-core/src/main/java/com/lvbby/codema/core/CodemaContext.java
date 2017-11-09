package com.lvbby.codema.core;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.lvbby.codema.core.bean.CodemaBeanFactory;
import com.lvbby.codema.core.bean.DefaultCodemaBeanFactory;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 16/12/23.
 */
public class CodemaContext {
    private Object source;
    private CodemaBeanFactory                           codemaBeanFactory = new DefaultCodemaBeanFactory();
    //顺序的MultiMap
    private Multimap<CodemaMachine, CommonCodemaConfig> runMap            = LinkedHashMultimap.create();


    Map<Class, Object> paramMap = Maps.newConcurrentMap();


    public <T extends CommonCodemaConfig> T findConfig(Class<T> clz) {
        return (T) findConfigBlur(clz);
    }
    public <T> T findBeanBlur(Class<T> clz , String id){
        List<T> beans = codemaBeanFactory
                .getBeans(clz, codemaBean -> codemaBean.getId().contains(id));
        if(beans.isEmpty())
            return null;
        Validate.isTrue(beans.size()==1,"multi bean found for %s",id);
        return beans.get(0);
    }

    public Object findConfigBlur(Class clz) {
        List<CommonCodemaConfig> configs = runMap.values().stream().filter(commonCodemaConfig -> clz.equals(commonCodemaConfig.getClass())).collect(
                Collectors.toList());
        if (configs.size() > 1){
            throw new IllegalArgumentException(String.format("multi config found : %s", clz.getName()));
        }
        return configs.isEmpty() ? null : configs.get(0);
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

    public Multimap<CodemaMachine, CommonCodemaConfig> getRunMap() {
        return runMap;
    }
}
