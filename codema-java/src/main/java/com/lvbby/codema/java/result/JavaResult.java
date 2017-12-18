/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.lvbby.codema.java.result;

import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.java.entity.JavaClass;

/**
 *
 * @author dushang.lp
 * @version $Id: JavaResult.java, v 0.1 2017年12月18日 下午1:02 dushang.lp Exp $
 */
public class JavaResult extends BasicResult<JavaClass> {

    private JavaClass javaClass;

    public JavaResult(JavaClass javaClass) {
        this.javaClass = javaClass;
        result(javaClass);
    }

    @Override public String getString() {
        return super.getString();
    }
}