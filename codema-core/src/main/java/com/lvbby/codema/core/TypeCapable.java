package com.lvbby.codema.core;

import java.lang.reflect.ParameterizedType;

/**
 * Created by lipeng on 16/10/30.
 */
public class TypeCapable<T> {

    public Class<T> getType() {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<T>) (parameterizedType.getActualTypeArguments()[0]);
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
