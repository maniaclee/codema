package com.lvbby.codema.java.machine.impl;

import com.lvbby.codema.core.machine.AbstractSourceMachine;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaClassUtils;
import com.lvbby.codema.java.tool.JavaSrcLoader;

/**
 * 解析java class
 * @author dushang.lp
 * @version $Id: JavaSourceFromClassFullNameMachine.java, v 0.1 2018年01月19日 下午6:51 dushang.lp Exp $
 */
public class JavaSourceFromClassMachine extends AbstractSourceMachine<Class, JavaClass> {
    @Override
    public JavaClass genSource(Class source) throws Exception {
        return JavaClassUtils.fromClass(source);
    }
}