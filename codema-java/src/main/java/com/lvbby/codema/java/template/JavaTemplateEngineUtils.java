package com.lvbby.codema.java.template;

import com.lvbby.codema.java.entity.JavaType;

/**
 *
 * @author dushang.lp
 * @version $Id: JavaTemplateEngineUtils.java, v 0.1 2017-09-22 下午12:07 dushang.lp Exp $
 */
public class JavaTemplateEngineUtils {

    public  String getBoxingType(Class clz){
        return JavaType.getBoxingType(clz).getSimpleName();
    }
}