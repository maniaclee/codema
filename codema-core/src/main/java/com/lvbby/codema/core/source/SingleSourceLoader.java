package com.lvbby.codema.core.source;

import com.google.common.collect.Lists;

import java.util.List;

/**
 *
 * @author dushang.lp
 * @version $Id: SingleSourceLoader.java, v 0.1 2017-11-09 上午8:44 dushang.lp Exp $
 */
public class SingleSourceLoader<T> implements SourceLoader<T> {
    private T source;

    protected void setSource(T source) {
        this.source = source;
    }

    @Override public List<T> loadSource() throws Exception {
        return Lists.newArrayList(source);
    }

}