package com.lvbby.codema.java.machine;

import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.java.entity.JavaClass;

/**
 * 入参JavaClass
 * Created by dushang.lp on 2017/8/16.
 */
public abstract class AbstractJavaInputCodemaMachine
        extends AbstractJavaCodemaMachine<JavaClass, JavaClass> {

    @Override protected void doCode() throws Exception {
        handle(codeEach(source));
    }

    public abstract Result<JavaClass> codeEach(JavaClass cu) throws Exception;
}
