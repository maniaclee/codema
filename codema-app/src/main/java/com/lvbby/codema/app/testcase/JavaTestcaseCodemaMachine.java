package com.lvbby.codema.app.testcase;

import com.lvbby.codema.app.springboot.JavaSpringBootConfig;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.CodemaMachine;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.inject.JavaTemplate;
import com.lvbby.codema.java.inject.JavaTemplateInjector;
import com.lvbby.codema.java.inject.JavaTemplateParameter;
import com.lvbby.codema.java.machine.AbstractJavaCodemaMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaTestcaseCodemaMachine extends AbstractJavaCodemaMachine<JavaTestcaseCodemaConfig>{

    public void processSingle(CodemaContext codemaContext, @NotNull JavaTestcaseCodemaConfig config,  JavaClass cu) throws Exception {
        config.handle(codemaContext, config,
                new JavaTemplateResult(config, $src__name_Test.class, cu)
                        .bind("springBootConfig", codemaContext.getConfig(JavaSpringBootConfig.class))
                        .registerResult()
        );
    }

}
