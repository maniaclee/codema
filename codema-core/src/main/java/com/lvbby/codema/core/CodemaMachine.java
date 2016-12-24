package com.lvbby.codema.core;

/**
 * Created by lipeng on 16/12/23.
 * 代码机的处理器，只处理
 */
public interface CodemaMachine {

    void code(CodemaContext codemaContext) throws Exception;
}
