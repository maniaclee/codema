package com.lvbby.codema.java.machine.impl;

import com.lvbby.codema.core.machine.AbstractSourceMachine;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaClassUtils;
import com.lvbby.codema.java.tool.JavaSrcLoader;

/**
 * 解析java 全类名
 * @author dushang.lp
 * @version $Id: JavaSourceFromClassFullNameMachine.java, v 0.1 2018年01月19日 下午6:51 dushang.lp Exp $
 */
public class JavaSourceFromClassFullNameMachine extends AbstractSourceMachine<String, JavaClass> {
    @Override
    public JavaClass genSource(String source) {
        return JavaClassUtils.convert(JavaSrcLoader.getJavaSrcCompilationUnit(source));
    }
}