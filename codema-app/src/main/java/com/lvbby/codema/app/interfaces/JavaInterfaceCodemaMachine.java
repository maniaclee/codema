package com.lvbby.codema.app.interfaces;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.inject.JavaTemplate;
import com.lvbby.codema.java.inject.JavaTemplateInjector;
import com.lvbby.codema.java.inject.JavaTemplateParameter;
import com.lvbby.codema.java.result.JavaTemplateResult;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaInterfaceCodemaMachine implements CodemaInjectable {

    @ConfigBind(JavaInterfaceCodemaConfig.class)
    @CodemaRunner
    @JavaTemplate
    public void code(CodemaContext codemaContext, @NotNull JavaInterfaceCodemaConfig config, @NotNull @JavaTemplateParameter(identifier = JavaTemplateInjector.java_source) JavaClass javaClass) throws Exception {
        config.handle(codemaContext, config, new JavaTemplateResult(config, "templates/$Interface_.java", javaClass)
                .bind("Interface", config.evalDestClassName(javaClass, javaClass.getName() + "Service"))
                .registerResult());
    }
}
