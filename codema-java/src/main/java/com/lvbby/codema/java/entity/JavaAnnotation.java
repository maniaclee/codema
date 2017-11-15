package com.lvbby.codema.java.entity;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dushang.lp on 2017/6/26.
 */
public class JavaAnnotation {
    public static final String defaultPropertyName  = "value";
    private String name;
    private Multimap<String, Object> properties = ArrayListMultimap.create();

    public JavaAnnotation(String name) {
        this.name = name;
    }

    public JavaAnnotation add(String key, Object value) {
        properties.put(key, value);
        return this;
    }

    public <T> T get(String key) {
        Collection<Object> objects = properties.get(key);
        return CollectionUtils.isEmpty(objects) ? null : (T) objects.iterator().next();
    }

    public <T> List<T> getList(String key) {
        Collection<Object> objects = properties.get(key);
        if (CollectionUtils.isEmpty(objects))
            return Lists.newLinkedList();
        return objects.stream().map(o -> (T) o).collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Multimap<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Multimap<String, Object> properties) {
        this.properties = properties;
    }

    private String parseValue(Object s ){
        if(s instanceof String){
            return String.format("\"%s\"", s);
        }
        if(s instanceof Class){
            return String.format("%s.class", ((Class) s).getName());
        }
        return s.toString();
    }
    @Override public String toString() {
        if(properties.isEmpty()){
            return String.format("@%s", name);
        }
        String re = properties.keySet().stream().map(s -> {
            Collection<Object> objects = properties.get(s);
            if (CollectionUtils.isNotEmpty(objects)) {
                if (objects.size() == 1) {
                    return String.format("%s=%s", s, parseValue(objects.iterator().next()));
                }
                return String.format("%s={%s}", s,
                        objects.stream().map(v->parseValue(v)).collect(Collectors.joining(",")));
            }
            return "";
        }).collect(Collectors.joining(","));
        return String.format("@%s(%s)", name,re);
    }
}
