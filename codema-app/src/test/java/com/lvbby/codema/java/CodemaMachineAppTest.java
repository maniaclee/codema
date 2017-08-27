package com.lvbby.codema.java;

import com.google.common.collect.Lists;
import com.lvbby.codema.app.bean.JavaBeanCodemaConfig;
import com.lvbby.codema.app.mvn.MavenConfig;
import com.lvbby.codema.app.mybatis.JavaMybatisCodemaConfig;
import com.lvbby.codema.core.Codema;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.handler.FileWriterResultHandler;
import com.lvbby.codema.core.handler.PrintResultHandler;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.source.JavaClassSourceParser;
import com.lvbby.codema.java.tool.JavaSrcLoader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Created by dushang.lp on 2017/6/26.
 */
public class CodemaMachineAppTest extends BaseTest {

    @Before
    public void init() {
        JavaSrcLoader.initJavaSrcRoots(Lists.newArrayList(new File(System.getProperty("user.home"), "workspace")));
    }

    private void exec(CommonCodemaConfig config) throws Exception {
        Codema.exec(config);
    }

    @Test
    public void mybatis() throws Exception {

        CommonCodemaConfig config = new CommonCodemaConfig();
        config.setAuthor("lee");
        config.addResultHandler(PrintResultHandler.class).addResultHandler(FileWriterResultHandler.class);

        MavenConfig mavenConfig = config.copy(MavenConfig.class);
        mavenConfig.setDestRootDir("~/workspace/");
        mavenConfig.setName("test-codema");
        mavenConfig.setGroupId("lvbby");
        mavenConfig.setArtifactId(mavenConfig.getName());
        mavenConfig.initMaven();

        JavaBasicCodemaConfig javaConfig = config.copy(JavaBasicCodemaConfig.class);
        javaConfig.setFromPackage("com.lvbby");
        javaConfig.setDestRootDir(mavenConfig.getDestSrcRoot());
        javaConfig.setDestResourceRoot(mavenConfig.getDestResourceRoot());
        javaConfig.setDestSrcRoot(mavenConfig.getDestSrcRoot());


        JavaMybatisCodemaConfig javaMybatisCodemaConfig = javaConfig.copy(JavaMybatisCodemaConfig.class);
        javaMybatisCodemaConfig.setIdQuery(javaClass -> javaClass.getFields().stream().filter(javaField -> javaField.getName().equals("destPackage")).findAny().orElse(null));
        javaMybatisCodemaConfig.setMapperDir("mapper");
        javaMybatisCodemaConfig.setDestPackage("com.lvbby");
        javaMybatisCodemaConfig.setConfigPackage("com.lvbby.config");
        JavaBeanCodemaConfig beanCodemaConfig = javaConfig.copy(JavaBeanCodemaConfig.class);
        beanCodemaConfig.setDestPackage("com.lvbby.bean");


        new Codema()
                .source(JavaClassSourceParser.fromClass(JavaBasicCodemaConfig.class))
                .bind(beanCodemaConfig)
                .bind(javaMybatisCodemaConfig)
                .bind(mavenConfig)
                .run();
    }

}
