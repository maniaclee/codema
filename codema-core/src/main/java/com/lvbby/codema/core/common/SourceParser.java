package com.lvbby.codema.core.common;

import com.lvbby.codema.core.CodemaMachine;

/**
 * Created by lipeng on 2016/12/24.
 */
public interface SourceParser extends CodemaMachine {

    /***
     * 定义支持的uri的 scheme和authority ， 如schema://authority/
     * 如果schema下的所有authority都支持，则返回schema://
     * @return
     */
    String getSupportedUriScheme();

}
