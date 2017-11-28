package com.lvbby.codema.core.result;


import java.io.InputStream;

/**
 * Created by lipeng on 2017/9/12.
 */
public interface MergeCapableFileResult<T> extends FileResult<T> {

    /***
     *获取merge后的result
     */
    String parseMergeResult(InputStream dest) throws Exception;
}
