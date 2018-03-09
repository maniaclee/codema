package com.lvbby.codema.java.machine.impl;

import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaClassUtils;
import com.lvbby.codema.java.tool.JavaLexer;

/**
 * 解析java源码
 * @author dushang.lp
 * @version $Id: JavaFromSrcMachine.java, v 0.1 2018年01月19日 下午6:41 dushang.lp Exp $
 */
public class JavaSourceFromSrcMachine extends AbstractBaseMachine<String, JavaClass> {
    @Override
    protected void doCode() throws Exception {
        handle(BasicResult.instance(JavaClassUtils.convert(JavaLexer.read(source))));
    }
}