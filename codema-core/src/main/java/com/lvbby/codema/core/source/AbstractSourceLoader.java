package com.lvbby.codema.core.source;

import java.util.List;

/**
 * @author dushang.lp
 * @version $Id: AbstractSourceLoader.java, v 0.1 2017-08-23 下午8:08 dushang.lp Exp $
 */
public abstract class AbstractSourceLoader<T> implements SourceLoader<T> {

    protected List<T> source;

    protected void setSource(List<T> source) {
        this.source = source;
    }

    @Override public List<T> loadSource() throws Exception {
        return source;
    }
}