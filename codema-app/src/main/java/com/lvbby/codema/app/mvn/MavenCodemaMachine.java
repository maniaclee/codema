package com.lvbby.codema.app.mvn;

import com.lvbby.codema.core.AbstractCodemaMachine;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.render.XmlTemplateResult;
import com.lvbby.codema.core.result.BasicResult;
import org.apache.commons.collections.CollectionUtils;

/**
 * Created by lipeng on 16/12/23.
 */
public class MavenCodemaMachine extends AbstractCodemaMachine<MavenConfig> {

    public void code(CodemaContext codemaContext, MavenConfig config) throws Exception {
        config.init();
        //创建目录，写入pom.xml,.gitignore
        process(codemaContext, config);

        /** .git ignore */
        config.handle(codemaContext, new BasicResult()
                .result(loadResourceAsString(".gitignore"))
                .config(config)
                .destFile(".gitignore")
        );
    }

    private void process(CodemaContext codemaContext, MavenConfig c) throws Exception {
        c.handle(codemaContext,
                new XmlTemplateResult(loadResourceAsString("pom.xml"))
                        .bind("config", c)
                        .destFile("pom.xml")
                        .config(c)
        );
        if (CollectionUtils.isNotEmpty(c.getModules())) {
            for (MavenConfig mavenConfig : c.getModules()) {
                process(codemaContext, mavenConfig);
            }
        }
    }

}
