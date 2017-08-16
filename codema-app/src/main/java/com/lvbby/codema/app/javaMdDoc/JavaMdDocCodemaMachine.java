package com.lvbby.codema.app.javaMdDoc;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.core.render.TemplateEngineResult;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.inject.JavaTemplateInjector;
import com.lvbby.codema.java.inject.JavaTemplateParameter;

/**
 * Created by dushang.lp on 2017/8/16.
 */
public class JavaMdDocCodemaMachine implements CodemaInjectable {
    @CodemaRunner
    @ConfigBind(JavaMdDocCodemaConfig.class)
    public void code(CodemaContext codemaContext, @NotNull JavaMdDocCodemaConfig config,@NotNull @JavaTemplateParameter(identifier = JavaTemplateInjector.java_source) JavaClass cu) throws Exception {

        config.handle(codemaContext, config, TemplateEngineResult
                .ofResource(TemplateEngineResult.class,getClass(),
                        "javaMdDoc.btl",
                        config.getDestFile()));
    }
}
