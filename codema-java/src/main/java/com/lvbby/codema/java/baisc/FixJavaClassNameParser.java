/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.lvbby.codema.java.baisc;

import com.lvbby.codema.java.entity.JavaClass;

/**
 *
 * @author dushang.lp
 * @version $Id: FixJavaClassNameParser.java, v 0.1 2017-08-27 обнГ5:51 dushang.lp Exp $
 */
public class FixJavaClassNameParser implements JavaClassNameParser<JavaClass> {
    private String className;

    public FixJavaClassNameParser(String className) {
        this.className = className;
    }

    @Override public String getClassName(JavaClass source) {
        return className;
    }
}