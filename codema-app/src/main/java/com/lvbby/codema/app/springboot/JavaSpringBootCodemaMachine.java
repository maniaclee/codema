package com.lvbby.codema.app.springboot;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.CodemaMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaSpringBootCodemaMachine implements CodemaMachine<JavaSpringBootConfig> {

    public void code(CodemaContext codemaContext, JavaSpringBootConfig config) throws Exception {
        config.handle(codemaContext, new JavaTemplateResult(config, MainApp.class));
    }

}
