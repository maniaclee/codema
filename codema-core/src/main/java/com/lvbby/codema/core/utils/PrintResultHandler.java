package com.lvbby.codema.core.utils;

import com.lvbby.codema.core.ResultContext;
import com.lvbby.codema.core.ResultHandler;

/**
 * Created by lipeng on 2016/12/26.
 */
public class PrintResultHandler implements ResultHandler {
    @Override
    public void handle(ResultContext resultContext) {
        System.out.println(resultContext.getResult());
    }
}
