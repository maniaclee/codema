package com.lvbby.codema.app.interfaces;

import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaInputCodemaMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;

/**
 * Created by lipeng on 17/1/6.
 */
public class JavaInterfaceCodemaConfig extends AbstractJavaInputCodemaMachine {
    @Override public Result<JavaClass> codeEach(JavaClass cu) throws Exception {
        return new JavaTemplateResult(this, $Interface_.class, cu);
    }
}
