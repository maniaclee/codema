package com.lvbby.codema.core.bean;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2016/12/31.
 */
public class DefaultCodemaBeanFactory implements CodemaBeanFactory {
    List<CodemaBean> resources = Lists.newArrayList();

    @Override
    public void register(CodemaBean resource) {
        if (resource == null)
            return;
        if (!resource.isValid())
            throw new IllegalArgumentException("invalid bean " + JSON.toJSONString(resource));
        if (resources.stream().anyMatch(e -> Objects.equals(e.getId(), resource.getId())))
            throw new IllegalArgumentException("bean existed for id : " + resource.getId());
        resources.add(resource);
    }

    @Override
    public <T> T getBean(String id) {
        if (StringUtils.isBlank(id))
            return null;
        List<CodemaBean> collect = resources.stream().filter(e -> e.match(id)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect))
            return null;
        if (collect.size() != 1)
            throw new IllegalArgumentException("multi resources found for id :" + id);
        return (T) collect.get(0).getResource();
    }

    @Override
    public <T> List<T> getBeans(Predicate<CodemaBean> predicate , Class<T> clz){
        return resources.stream().filter(predicate).filter(codemaBean -> clz.isAssignableFrom(codemaBean.getResource().getClass())).map(codemaBean -> (T)codemaBean.getResource()).collect(Collectors.toList());
    }

    @Override
    public <T> T getBean(Class<T> type) {
        List<T> beans = getBeans(type);
        if (beans.size() != 1)
            throw new IllegalArgumentException("multi resources found for type :" + type.getName());
        return (T) beans.get(0);
    }

    @Override
    public <T> List<T> getBeans(Class<T> type) {
        return resources.stream().filter(e -> e.match(type)).map(e -> (T) e.getResource()).collect(Collectors.toList());
    }

}
