package com.lvbby.codema.core.utils;

/**
 * 可以抛异常的function
 * @author dushang.lp
 * @version $Id: FunctionAdaptor.java, v 0.1 2017-12-02 下午9:53 dushang.lp Exp $
 */
public interface FunctionAdaptor<S, O> {
    O apply(S source) throws Exception;
}