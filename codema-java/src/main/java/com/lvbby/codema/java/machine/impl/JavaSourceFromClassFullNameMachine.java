package com.lvbby.codema.java.machine.impl;

import com.lvbby.codema.core.machine.AbstractSourceMachine;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaClassUtils;
import com.lvbby.codema.java.tool.JavaSrcLoader;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

/**
 * 解析java 全类名
 * 支持形式：className#method
 * @author dushang.lp
 * @version $Id: JavaSourceFromClassFullNameMachine.java, v 0.1 2018年01月19日 下午6:51 dushang.lp Exp $
 */
public class JavaSourceFromClassFullNameMachine extends AbstractSourceMachine<String, JavaClass> {
    @Override public JavaClass genSource(String source) {
        String method = null;
        if (source.contains("#")) {
            int i = source.indexOf("#");
            Validate.isTrue(i < source.length() - 1 && i > 0, "invalid class full name: %s", source);
            method = source.substring(i + 1);
            source = source.substring(0, i);
        }
        JavaClass result = JavaClassUtils.convert(JavaSrcLoader.getJavaSrcCompilationUnit(source));
        String finalMethod = method;
        result.removeMethod(s -> !StringUtils.equals(finalMethod, s));
        return result;
    }
}