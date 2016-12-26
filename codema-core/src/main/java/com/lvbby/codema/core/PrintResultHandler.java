package com.lvbby.codema.core;

import com.lvbby.codema.core.config.CommonCodemaConfig;

/**
 * Created by lipeng on 2016/12/26.
 */
public class PrintResultHandler implements ResultHandler<CommonCodemaConfig> {
    @Override
    public void handle(CodemaContext codemaContext, CommonCodemaConfig config, Object result) {
        System.out.println(result);
    }
}
