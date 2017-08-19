package com.lvbby.codema.core.source;

/**
 * Created by lipeng on 2017/8/19.
 */
public interface SourceLoader<T> {

    T loadSource() throws Exception;
}
