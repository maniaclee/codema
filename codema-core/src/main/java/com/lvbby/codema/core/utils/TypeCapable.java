package com.lvbby.codema.core.utils;

import com.google.common.collect.Lists;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 16/10/30.
 */
public class TypeCapable<T> {

    public Class<T> getType() {
        return (Class<T>) (getTypes().get(0));
    }

    public Class<T> getType(int i) {
        return (Class<T>) (getTypes().get(i));
    }

    public List<Class> getTypes() {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        return Lists.newArrayList(parameterizedType.getActualTypeArguments()).stream().map(type -> (Class<?>) type).collect(Collectors.toList());
    }

    /***
     * if the obj is instance of this type
     *
     * @param obj can't not be null
     * @return
     */
    public boolean isType(Object obj) {
        return getType().isAssignableFrom(obj.getClass());
    }

    public boolean isType(int i, Object obj) {
        return getType(i).isAssignableFrom(obj.getClass());
    }
}
