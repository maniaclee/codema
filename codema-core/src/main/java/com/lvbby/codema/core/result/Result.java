package com.lvbby.codema.core.result;

/**
 * Created by lipeng on 2017/1/3.
 */
public interface Result<T> {
    Result<T> of(T t);

    T getResult();

    Class<T> getResultType();
}
