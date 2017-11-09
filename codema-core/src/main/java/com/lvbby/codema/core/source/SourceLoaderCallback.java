package com.lvbby.codema.core.source;

import com.lvbby.codema.core.CodemaContext;

/**
 *SourceLoader ������sourceʱ�Ļص�
 * @author dushang.lp
 * @version $Id: SourceLoaderCallback.java, v 0.1 2017-11-09 ����9:00 dushang.lp Exp $$
 */
public interface SourceLoaderCallback<T> {
    void process(T source , CodemaContext context);
}