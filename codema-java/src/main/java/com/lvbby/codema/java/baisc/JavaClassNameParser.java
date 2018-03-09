package com.lvbby.codema.java.baisc;

/**
 *
 * @author dushang.lp
 * @version $Id: JavaClassNameParser.java, v 0.1 2017-08-27 下午5:49 dushang.lp Exp $$
 */
public interface JavaClassNameParser<T> {
    /***
     *
     * @param source 来源
     * @return
     */
    String getClassName(T source );
}