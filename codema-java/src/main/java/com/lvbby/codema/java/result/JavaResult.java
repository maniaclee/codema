package com.lvbby.codema.java.result;

import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.java.entity.JavaClass;

/**
 *
 * @author dushang.lp
 * @version $Id: JavaResult.java, v 0.1 2017年12月18日 下午1:02 dushang.lp Exp $
 */
public class JavaResult extends BasicResult<JavaClass> {

    public JavaResult(JavaClass javaClass) {
        result(javaClass);
    }

}