package com.lvbby.codema.core;

import com.lvbby.codema.core.config.CommonCodemaConfig;

/**
 * Created by lipeng on 16/12/23.
 * 代码机的处理器，只处理
 */
public interface CodemaMachine<T extends CommonCodemaConfig> {

    void code(CodemaContext codemaContext, T config) throws Exception;
}
