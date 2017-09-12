package com.lvbby.codema.core.result;


import com.lvbby.codema.core.ResultContext;

import java.io.InputStream;

/**
 * Created by lipeng on 2017/9/12.
 */
public interface MergeCapableFileResult extends FileResult {

    /***
     *获取merge后的result
     */
    String parseMergeResult(InputStream dest, ResultContext resultContext) throws Exception;
}
