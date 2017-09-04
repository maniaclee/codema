package com.lvbby.codema.java.baisc;

import com.lvbby.codema.java.entity.JavaClass;

/**
 *
 * @author dushang.lp
 * @version $Id: JavaClassNameParser.java, v 0.1 2017-08-27 下午5:49 dushang.lp Exp $$
 */
public interface JavaClassNameParser {
    /***
     *
     * @param source 来源
     * @param from  要处理的JavaClass
     * @return
     */
    String getClassName(JavaClass source , JavaClass from);
}