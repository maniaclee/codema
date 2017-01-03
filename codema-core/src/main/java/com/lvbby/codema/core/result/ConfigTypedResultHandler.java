package com.lvbby.codema.core.result;

import com.lvbby.codema.core.ResultContext;
import com.lvbby.codema.core.ResultHandler;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.utils.TypeCapable;

/**
 * Created by lipeng on 17/1/3.
 */
public abstract class ConfigTypedResultHandler<T extends CommonCodemaConfig> extends TypeCapable<T> implements ResultHandler {
    @Override
    public void handle(ResultContext resultContext) {
        if (isType(resultContext.getConfig()))
            process(resultContext, (T) resultContext.getConfig());
    }

    protected abstract void process(ResultContext resultContext, T config);
}
