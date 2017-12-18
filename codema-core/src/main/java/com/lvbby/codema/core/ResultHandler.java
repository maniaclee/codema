package com.lvbby.codema.core;

import com.lvbby.codema.core.result.Result;

/**
 * Created by lipeng on 2016/12/26.
 */
public interface ResultHandler {

    void handle(Result result) throws Exception;
}
