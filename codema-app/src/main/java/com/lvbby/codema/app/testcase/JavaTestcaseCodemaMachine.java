package com.lvbby.codema.app.testcase;

import com.lvbby.codema.app.springboot.JavaSpringBootConfig;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaCodemaMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaTestcaseCodemaMachine extends AbstractJavaCodemaMachine<JavaTestcaseCodemaConfig> {

    public void codeEach(CodemaContext codemaContext, JavaTestcaseCodemaConfig config, JavaClass cu) throws Exception {
        config.handle(codemaContext,
                new JavaTemplateResult(config, $src__name_Test.class, cu)
                        .bind("springBootConfig", codemaContext.getCodema().findConfig(JavaSpringBootConfig.class))
        );
    }

}
