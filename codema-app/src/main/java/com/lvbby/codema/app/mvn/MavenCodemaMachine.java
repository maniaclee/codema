package com.lvbby.codema.app.mvn;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.CodemaMachine;
import com.lvbby.codema.core.render.TemplateEngineResult;
import com.lvbby.codema.core.render.XmlTemplateResult;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.core.utils.CodemaUtils;

/**
 * Created by lipeng on 16/12/23.
 */
public class MavenCodemaMachine implements CodemaMachine<MavenConfig> {

    public void code(CodemaContext codemaContext, MavenConfig config) throws Exception {
        for (MavenConfig c : CodemaUtils.getAllConfigWithAnnotation(config)) {
            config.handle(codemaContext,
                    TemplateEngineResult.ofResource(
                            XmlTemplateResult.class,
                            MavenCodemaMachine.class,
                            "pom.xml",
                            c.findRootDir().getAbsolutePath())
                            .bind("config", c));
        }

        /** .git ignore */
        config.handle(codemaContext, BasicResult.ofResource(getClass(), ".gitignore", config.findRootDir().getAbsolutePath()));
    }

}
