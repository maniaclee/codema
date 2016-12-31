package com.lvbby.codema.core.utils;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.ResultHandler;

/**
 * Created by lipeng on 2016/12/26.
 */
public class PrintResultHandler implements ResultHandler {
    @Override
    public void handle(CodemaContext codemaContext, Object result) {
        System.out.println(result);
    }
}
