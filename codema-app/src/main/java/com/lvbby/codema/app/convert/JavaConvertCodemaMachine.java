package com.lvbby.codema.app.convert;

import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaInputCodemaMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;

/**
 * Created by lipeng on 16/12/27.
 */
public class JavaConvertCodemaMachine extends AbstractJavaInputCodemaMachine {
    @Override public Result<JavaClass> codeEach(JavaClass cu) throws Exception {
        return new JavaTemplateResult(this, $Convert_.class, cu);
    }
}

