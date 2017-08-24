package com.lvbby.codema.core.result;

import com.lvbby.codema.core.ResultContext;
import com.lvbby.codema.core.ResultHandler;
import com.lvbby.codema.core.utils.TypeCapable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lipeng on 17/1/3.
 */
public abstract class AbstractResultHandler<C> extends TypeCapable<C> implements ResultHandler {
    protected static Logger logger = LoggerFactory.getLogger(AbstractResultHandler.class);

    @Override
    public void handle(ResultContext resultContext) throws Exception {
        if (isType(resultContext.getResult())) {
            process(resultContext, (C) resultContext.getResult());
        }
    }

    protected abstract void process(ResultContext resultContext, C result) throws Exception;

}
