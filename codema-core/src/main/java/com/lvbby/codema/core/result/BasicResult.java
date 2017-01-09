package com.lvbby.codema.core.result;

/**
 * Created by lipeng on 17/1/9.
 */
public class BasicResult implements PrintableResult {

    private Object result;

    public BasicResult result(Object result) {
        this.result = result;
        return this;
    }

    @Override
    public Object getResult() {
        return result;
    }

    @Override
    public String getString() {
        return result == null ? "" : result.toString();
    }
}
