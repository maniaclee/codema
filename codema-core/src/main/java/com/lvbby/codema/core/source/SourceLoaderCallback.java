package com.lvbby.codema.core.source;

import com.lvbby.codema.core.CodemaContext;

/**
 *SourceLoader 在生成source时的回调
 * @author dushang.lp
 * @version $Id: SourceLoaderCallback.java, v 0.1 2017-11-09 上午9:00 dushang.lp Exp $$
 */
public interface SourceLoaderCallback<T> {
    void process(T source , CodemaContext context);
}