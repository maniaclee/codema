package com.lvbby.codema.core;

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
}
