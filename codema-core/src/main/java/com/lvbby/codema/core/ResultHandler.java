package com.lvbby.codema.core;

import com.lvbby.codema.core.config.CommonCodemaConfig;

/**
 * Created by lipeng on 2016/12/26.
 */
public interface ResultHandler<T extends CommonCodemaConfig> {

    void handle(CodemaContext codemaContext, T config, Object result);
}
