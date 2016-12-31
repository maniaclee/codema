package com.lvbby.codema.core.resource;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2016/12/31.
 */
public class DefaultResourceLoader implements ResourceLoader {
    List<CodemaResource> resources = Lists.newArrayList();

    @Override
    public void register(CodemaResource resource) {
        if (resource != null)
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
        List<CodemaResource> collect = resources.stream().filter(e -> e.match(id)).collect(Collectors.toList());
        if (collect.size() != 1)
            throw new IllegalArgumentException("multi resources found for id :" + id);
        return (T) collect.get(0);
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
        return resources.stream().filter(e -> e.match(type)).map(e -> (T) e).collect(Collectors.toList());
    }

}
