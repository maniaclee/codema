package com.lvbby.codema.core.handler;

import com.lvbby.codema.core.ResultHandler;
import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.core.utils.TypeCapable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lipeng on 17/1/3.
 */
public abstract class AbstractResultHandler<C> extends TypeCapable<C> implements ResultHandler {
    protected static Logger logger = LoggerFactory.getLogger(AbstractResultHandler.class);

    @Override
    public void handle(Result result) throws Exception {
        if (isType(result.getResultType())) {
            process( (C) result);
        }
    }

    protected abstract void process(C result) throws Exception;

}
