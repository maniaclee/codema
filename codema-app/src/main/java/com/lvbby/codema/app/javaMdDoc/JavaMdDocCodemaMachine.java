package com.lvbby.codema.app.javaMdDoc;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.render.TemplateEngineResult;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaCodemaMachine;

/**
 * Created by dushang.lp on 2017/8/16.
 */
public class JavaMdDocCodemaMachine extends AbstractJavaCodemaMachine<JavaMdDocCodemaConfig> {

    public void codeEach(CodemaContext codemaContext, JavaMdDocCodemaConfig config, JavaClass cu) throws Exception {
        config.handle(codemaContext, TemplateEngineResult
                .ofResource(TemplateEngineResult.class, getClass(),
                        "javaMdDoc.btl",
                        config.getDestFile()));
    }
}
