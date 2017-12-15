package com.lvbby.codema.java.machine;

import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.result.JavaTemplateResult;

/**
 * 入参出参都是JavaClass
 * Created by dushang.lp on 2017/8/16.
 */
public abstract class JavaMachine extends AbstractJavaInputMachine<JavaClass> {
    @Override protected void doCode() throws Exception {
        handle(codeEach(source));
    }

    public abstract Result<JavaClass> codeEach(JavaClass cu) throws Exception;

    protected JavaTemplateResult buildJavaTemplateResult() {
        return new JavaTemplateResult(this, getTemplate(), getSource());
    }

}
