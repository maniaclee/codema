package com.lvbby.codema.core.result;

import com.lvbby.codema.core.utils.TypeCapable;

/**
 *
 * @author dushang.lp
 * @version $Id: AbstractResult.java, v 0.1 2017-11-28 上午8:43 dushang.lp Exp $
 */
public abstract class AbstractResult<T> extends TypeCapable<T> implements PrintableResult<T> {
    private T result;
    private boolean inited=false;

    @Override
    public Result<T> of(T t) {
        this.result = t;
        return this;
    }

    @Override
    public final void init() {
        if(inited)
            return;
        doInit();
        inited=true;
    }

    protected void doInit() {
    }

    @Override
    public final T getResult() {
        init();
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public final String getString() {
        init();
        return doGetString();
    }

    protected String doGetString(){
        return result ==null?null:result.toString();
    }

    @Override public Class<T> getResultType() {
        return getType();
    }

    @Override
    public String toString() {
        return getString();
    }
}