package com.lvbby.codema.java.machine.impl;

import com.lvbby.codema.core.machine.AbstractSourceMachine;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaClassUtils;
import com.lvbby.codema.java.tool.JavaLexer;

/**
 * 解析java源码
 * @author dushang.lp
 * @version $Id: JavaFromSrcMachine.java, v 0.1 2018年01月19日 下午6:41 dushang.lp Exp $
 */
public class JavaSourceFromSrcMachine extends AbstractSourceMachine<String, JavaClass> {
    @Override
    public JavaClass genSource(String source) {
        return JavaClassUtils.convert(JavaLexer.read(source));
    }
}