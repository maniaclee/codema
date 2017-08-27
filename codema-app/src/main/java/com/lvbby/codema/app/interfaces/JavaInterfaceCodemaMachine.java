package com.lvbby.codema.app.interfaces;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaCodemaMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaInterfaceCodemaMachine extends AbstractJavaCodemaMachine<JavaInterfaceCodemaConfig> {

    public void codeEach(CodemaContext codemaContext, JavaInterfaceCodemaConfig config, JavaClass javaClass) throws Exception {
        config.handle(codemaContext, new JavaTemplateResult(config, $Interface_.class, javaClass)
                .bind("Interface", config.evalDestClassName(javaClass, javaClass.getName() + "Service"))
        );
    }

}
