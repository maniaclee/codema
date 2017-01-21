package com.lvbby.codema.app.springboot;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.java.result.JavaTemplateResult;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaSpringBootCodemaMachine implements CodemaInjectable {
    @CodemaRunner
    @ConfigBind(JavaSpringBootConfig.class)
    public void code(CodemaContext codemaContext, @NotNull JavaSpringBootConfig config) throws Exception {
        config.handle(codemaContext, config, new JavaTemplateResult(config, "templates/MainApp.java")
                .registerResult());
    }

}
