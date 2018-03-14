package com.lvbby.codema.app.mvn;

import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.render.XmlTemplateResult;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.core.result.WriteMode;
import com.lvbby.codema.java.tool.MavenConfig;

/**
 * Created by lipeng on 16/12/23.
 */
public class MavenMachine extends AbstractBaseMachine<MavenConfig, MavenConfig> {

    public void doCode() throws Exception {
        source.visit(mavenConfig -> {

            try {
                //创建目录，写入pom.xml,.gitignore
                //设置result，传递给子maven
                /** .git ignore */
                handleSimple(new BasicResult().result(loadResourceAsString(".gitignore")).filePath(mavenConfig.getDestRootDir(), ".gitignore"));
                handleSimple(new XmlTemplateResult(loadResourceAsString("pom.xml")).bind("config", mavenConfig)
                        .filePath(mavenConfig.getDestRootDir(), "pom.xml").writeMode(WriteMode.writeIfNoExist));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

}
