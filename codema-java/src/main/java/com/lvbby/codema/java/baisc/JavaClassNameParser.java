package com.lvbby.codema.java.baisc;

import com.lvbby.codema.java.entity.JavaClass;

/**
 *
 * @author dushang.lp
 * @version $Id: JavaClassNameParser.java, v 0.1 2017-08-27 ����5:49 dushang.lp Exp $$
 */
public interface JavaClassNameParser {
    /***
     *
     * @param source ��Դ
     * @param from  Ҫ�����JavaClass
     * @return
     */
    String getClassName(JavaClass source , JavaClass from);
}