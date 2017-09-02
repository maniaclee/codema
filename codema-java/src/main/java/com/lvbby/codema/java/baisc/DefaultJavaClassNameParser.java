/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.lvbby.codema.java.baisc;

import com.lvbby.codema.java.entity.JavaClass;

/**
 *
 * @author dushang.lp
 * @version $Id: FixJavaClassNameParser.java, v 0.1 2017-08-27 5:51 dushang.lp Exp $
 */
public class DefaultJavaClassNameParser implements JavaClassNameParser<JavaClass> {

    @Override public String getClassName(JavaClass source) {
        return source.getName();
    }
}