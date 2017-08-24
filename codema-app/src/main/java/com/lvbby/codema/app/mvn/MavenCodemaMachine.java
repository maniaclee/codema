package com.lvbby.codema.app.mvn;

import com.lvbby.codema.core.AbstractCodemaMachine;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.CodemaMachine;
import com.lvbby.codema.core.render.TemplateEngineResult;
import com.lvbby.codema.core.render.XmlTemplateResult;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.core.utils.CodemaUtils;

/**
 * Created by lipeng on 16/12/23.
 */
public class MavenCodemaMachine extends AbstractCodemaMachine<MavenConfig> {

    public void code(CodemaContext codemaContext, MavenConfig config) throws Exception {
        for (MavenConfig c : CodemaUtils.getAllConfigWithAnnotation(config)) {
            config.handle(codemaContext,
                    new XmlTemplateResult()
                            .template(loadResourceAsString("pom.xml"))
                            .bind("config", c)
                            .destFile("pom.xml")
                            .config(c)
            );
        }

        /** .git ignore */
        config.handle(codemaContext, new BasicResult()
                .result(loadResourceAsString(".gitignore"))
                .config(config)
                .destFile(".gitignore")
        );
    }

}
