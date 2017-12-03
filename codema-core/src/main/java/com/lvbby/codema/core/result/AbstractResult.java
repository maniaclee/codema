package com.lvbby.codema.core.result;

import com.lvbby.codema.core.utils.TypeCapable;

/**
 *
 * @author dushang.lp
 * @version $Id: AbstractResult.java, v 0.1 2017-11-28 上午8:43 dushang.lp Exp $
 */
public abstract class AbstractResult<T> extends TypeCapable<T> implements PrintableResult<T> {
    private T result;
    private boolean needHandle=true;

    @Override public Result<T> of(T t) {
        this.result = t;
        return this;
    }

    @Override public T getResult() {
        return result;
    }

    @Override public String getString() {
        return result==null?null:result.toString();
    }

    @Override public Class<T> getResultType() {
        return getType();
    }
}