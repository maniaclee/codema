package com.lvbby.codema.core.result;

import com.lvbby.codema.core.ResultContext;
import com.lvbby.codema.core.ResultHandler;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.utils.TypeCapable;

/**
 * Created by lipeng on 17/1/3.
 */
public abstract class TypedResultHandler<C extends CommonCodemaConfig, R> extends TypeCapable<C> implements ResultHandler {
    @Override
    public void handle(ResultContext resultContext) {
        if (isType(0, resultContext.getConfig()) && isType(1, resultContext.getResult()))
            process(resultContext, (C) resultContext.getConfig(), (R) resultContext.getResult());
    }

    protected abstract void process(ResultContext resultContext, C config, R result);
}
