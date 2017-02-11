package com.lvbby.codema.core.handler;

import com.lvbby.codema.core.ResultContext;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.result.PrintableResult;
import com.lvbby.codema.core.result.TypedResultHandler;

/**
 * Created by lipeng on 2016/12/26.
 */
public class PrintResultHandler extends TypedResultHandler<CommonCodemaConfig, PrintableResult> {
    @Override
    protected void process(ResultContext resultContext, CommonCodemaConfig config, PrintableResult result) {
        System.out.println(result.getString());
    }
}
