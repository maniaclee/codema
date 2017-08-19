package com.lvbby.codema.app.delegate;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaCodemaMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaDelegateCodemaMachine extends AbstractJavaCodemaMachine<JavaDelegateCodemaConfig> {

    public void processSingle(CodemaContext codemaContext, JavaDelegateCodemaConfig config, JavaClass javaClass) throws Exception {
        config.handle(codemaContext, config, new JavaTemplateResult(config, $Delegate_.class, javaClass)
                .bind("Delegate", config.evalDestClassName(javaClass, javaClass.getName() + "Delegate"))
                .registerResult());
    }
}
