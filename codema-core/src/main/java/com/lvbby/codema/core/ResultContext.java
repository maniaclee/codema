package com.lvbby.codema.core;

import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.result.Result;

/**
 * Created by lipeng on 17/1/3.
 */
public class ResultContext {
    private CodemaContext codemaContext;
    private CommonCodemaConfig config;
    private Result result;

    public static ResultContext of(CodemaContext codemaContext, CommonCodemaConfig config, Result result) {
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

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
