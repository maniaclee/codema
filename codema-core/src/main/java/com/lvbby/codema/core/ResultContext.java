package com.lvbby.codema.core;

import com.lvbby.codema.core.config.CommonCodemaConfig;

/**
 * Created by lipeng on 17/1/3.
 */
public class ResultContext {
    private CodemaContext codemaContext;
    private CommonCodemaConfig config;
    private Object result;

    public static ResultContext of(CodemaContext codemaContext, CommonCodemaConfig config, Object result) {
        ResultContext resultContext = new ResultContext();
        resultContext.setCodemaContext(codemaContext);
        resultContext.setConfig(config);
        resultContext.setResult(result);
        return resultContext;
    }

    public CodemaContext getCodemaContext() {
        return codemaContext;
    }

    public void setCodemaContext(CodemaContext codemaContext) {
        this.codemaContext = codemaContext;
    }

    public CommonCodemaConfig getConfig() {
        return config;
    }

    public void setConfig(CommonCodemaConfig config) {
        this.config = config;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
