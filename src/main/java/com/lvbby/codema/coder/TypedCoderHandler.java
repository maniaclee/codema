package com.lvbby.codema.coder;

import java.lang.reflect.ParameterizedType;

/**
 * Created by lipeng on 2016/12/20.
 */
public abstract class TypedCoderHandler<T extends CoderBaseRequest> implements CoderHandler {

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

    @Override
    public void handle(CoderBaseRequest context) throws Exception {
        if (isType(context))
            process((T) context);
    }

    public abstract void process(T request) throws Exception;
}
