package com.lvbby.codema.java.baisc;

/**
 *
 * @author dushang.lp
 * @version $Id: JavaClassNameParser.java, v 0.1 2017-08-27 обнГ5:49 dushang.lp Exp $$
 */
public interface JavaClassNameParser<T> {
    String getClassName(T source);
}