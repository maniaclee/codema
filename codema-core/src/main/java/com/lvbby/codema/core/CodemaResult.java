package com.lvbby.codema.core;

/**
 * Created by lipeng on 2016/12/26.
 */
public class CodemaResult {

    private Object result;

    private ResultHandler resultHandler;

    public static CodemaResult of(Object result) {
        CodemaResult re = new CodemaResult();
        re.setResult(result);
        return re;
    }

    public CodemaResult resultHandler(ResultHandler resultHandler) {
        setResultHandler(resultHandler);
        return this;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public ResultHandler getResultHandler() {
        return resultHandler;
    }


    public void setResultHandler(ResultHandler resultHandler) {
        this.resultHandler = resultHandler;
    }
}
