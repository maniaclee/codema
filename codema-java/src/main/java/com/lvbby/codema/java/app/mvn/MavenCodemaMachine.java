package com.lvbby.codema.java.app.mvn;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.core.result.BasicResult;
import org.apache.commons.collections.CollectionUtils;

/**
 * Created by lipeng on 16/12/23.
 */
public class MavenCodemaMachine implements CodemaInjectable {
    @CodemaRunner
    @ConfigBind(MavenConfig.class)
    public void code(CodemaContext codemaContext, @NotNull MavenConfig config) throws Exception {
        initConfig(null, config);
        handle(codemaContext, config);
    }

    private void handle(CodemaContext codemaContext, MavenConfig config) throws Exception {
        if (config == null)
            return;
        doHandle(codemaContext, config);
        if (CollectionUtils.isNotEmpty(config.getModules()))
            for (MavenConfig mavenConfig : config.getModules())
                handle(codemaContext, mavenConfig);
    }

    private void doHandle(CodemaContext codemaContext, MavenConfig config) throws Exception {
        System.err.println("---------->  "+config.getName());
        config.handle(codemaContext, config, BasicResult.ofResource(MavenCodemaMachine.class, "pom.xml", config.findRootDir().getAbsolutePath()));
    }

    private void initConfig(MavenConfig parent, MavenConfig child) {
        child.setParent(parent);
        if (CollectionUtils.isNotEmpty(child.getModules()))
            for (MavenConfig mavenConfig : child.getModules())
                initConfig(child, mavenConfig);
    }
}
