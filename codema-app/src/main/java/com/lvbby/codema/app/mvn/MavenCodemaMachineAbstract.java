package com.lvbby.codema.app.mvn;

import com.lvbby.codema.core.AbstractVoidCodemaMachine;
import com.lvbby.codema.core.render.XmlTemplateResult;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.core.result.WriteMode;
import org.apache.commons.collections.CollectionUtils;

/**
 * Created by lipeng on 16/12/23.
 */
public class MavenCodemaMachineAbstract extends AbstractVoidCodemaMachine<MavenConfig> {

    public void codeEach(MavenConfig config) throws Exception {
        config.init();
        //创建目录，写入pom.xml,.gitignore
        process(config);

        /** .git ignore */
        config.handle(new BasicResult()
                .result(loadResourceAsString(".gitignore"))
                .config(config)
                .destFile(".gitignore")
        );
    }

    private void process( MavenConfig c) throws Exception {
        c.handle(new XmlTemplateResult(loadResourceAsString("pom.xml"))
                        .bind("config", c)
                        .destFile("pom.xml")
                        .config(c)
                        .writeMode(WriteMode.writeIfNoExist)
        );
        if (CollectionUtils.isNotEmpty(c.getModules())) {
            for (MavenConfig mavenConfig : c.getModules()) {
                process(mavenConfig);
            }
        }
    }

}
