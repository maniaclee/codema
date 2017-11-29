package com.lvbby.codema.app.delegate;

import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaInputCodemaMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;

/**
 * Created by lipeng on 17/1/6.
 */
public class JavaDelegateCodemaConfig extends AbstractJavaInputCodemaMachine {
    @Override public Result<JavaClass> codeEach(JavaClass cu) throws Exception {
        return new JavaTemplateResult(this, $Delegate_.class, cu);
    }
}
