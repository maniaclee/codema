package com.lvbby.codema.java.app.mvn;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.core.render.TemplateEngineResult;
import com.lvbby.codema.core.render.XmlTemplateResult;
import com.lvbby.codema.core.utils.CodemaUtils;
import org.apache.commons.collections.CollectionUtils;

/**
 * Created by lipeng on 16/12/23.
 */
public class MavenCodemaMachine implements CodemaInjectable {
    @CodemaRunner
    @ConfigBind(MavenConfig.class)
    public void code(CodemaContext codemaContext, @NotNull MavenConfig config) throws Exception {
        initConfig(null, config);
        for (MavenConfig c : CodemaUtils.getAllConfigWithAnnotation(config)) {
            config.handle(codemaContext, c, TemplateEngineResult.ofResource(XmlTemplateResult.class, MavenCodemaMachine.class, "pom.xml", c.findRootDir().getAbsolutePath()));
        }
    }

    private void initConfig(MavenConfig parent, MavenConfig child) {
        child.setParent(parent);
        if (CollectionUtils.isNotEmpty(child.getModules()))
            for (MavenConfig mavenConfig : child.getModules())
                initConfig(child, mavenConfig);
    }
}
