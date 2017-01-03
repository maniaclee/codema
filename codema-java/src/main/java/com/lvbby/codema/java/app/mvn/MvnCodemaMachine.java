package com.lvbby.codema.java.app.mvn;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.ResultContext;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.core.render.TemplateEngineResult;
import org.apache.commons.collections.CollectionUtils;

/**
 * Created by lipeng on 16/12/23.
 */
public class MvnCodemaMachine implements CodemaInjectable {
    private static String newVarNameDefault = "result";

    @CodemaRunner
    public void code(CodemaContext codemaContext, @NotNull MavenConfig config) {
        initConfig(null, config);
        handle(codemaContext, config);
    }

    private void handle(CodemaContext codemaContext, @NotNull MavenConfig config) {
        if (config != null)
            config.handle(ResultContext.of(codemaContext, config, new TemplateEngineResult("", config)));
        if (CollectionUtils.isNotEmpty(config.getModules()))
            for (MavenConfig mavenConfig : config.getModules())
                handle(codemaContext, mavenConfig);
    }

    private void initConfig(MavenConfig parent, MavenConfig child) {
        child.setParent(parent);
        for (MavenConfig mavenConfig : child.getModules())
            initConfig(child, mavenConfig);
    }

}
