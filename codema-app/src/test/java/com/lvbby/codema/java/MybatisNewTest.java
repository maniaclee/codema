package com.lvbby.codema.java;

import com.google.common.collect.Lists;
import com.lvbby.codema.app.bean.JavaBeanCodemaConfig;
import com.lvbby.codema.app.mvn.MavenConfig;
import com.lvbby.codema.app.mybatis.MybatisCodemaConfig;
import com.lvbby.codema.core.Codema;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.handler.FileWriterResultHandler;
import com.lvbby.codema.core.handler.PrintResultHandler;
import com.lvbby.codema.core.resource.ClassPathResource;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.baisc.JavaClassNameParserFactory;
import com.lvbby.codema.java.result.JavaRegisterResultHandler;
import com.lvbby.codema.java.source.JavaDbSourceParser;
import com.lvbby.codema.java.tool.JavaSrcLoader;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSetMetaData;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by dushang.lp on 2017/6/26.
 */
public class MybatisNewTest extends BaseTest {

    @Before
    public void init() {
        JavaSrcLoader.initJavaSrcRoots(Lists.newArrayList(new File(System.getProperty("user.home"), "workspace")));
    }

    @Test
    public void mybatis() throws Exception {

        CommonCodemaConfig config = new CommonCodemaConfig();
        config.setAuthor("lee");
        config
//                .addResultHandler(PrintResultHandler.class)
//                .addResultHandler(FileWriterResultHandler.class)
                .addResultHandler(JavaRegisterResultHandler.class)
        ;

        MavenConfig mavenConfig = config.copy(MavenConfig.class);
        mavenConfig.setDestRootDir("~/workspace/");
        mavenConfig.setName("test-codema");
        mavenConfig.setGroupId("lvbby");
        mavenConfig.setArtifactId(mavenConfig.getName());
        mavenConfig.initMaven();

        JavaBasicCodemaConfig java = config.copy(JavaBasicCodemaConfig.class);
        java.setDestPackage("com.lvbby.mybatis");
        java.setDestRootDir(mavenConfig.getDestSrcRoot());
        java.setDestResourceRoot(mavenConfig.getDestResourceRoot());
        java.setDestSrcRoot(mavenConfig.getDestSrcRoot());

        //entity
        JavaBeanCodemaConfig beanCodemaConfig = java.copy(JavaBeanCodemaConfig.class);
        beanCodemaConfig.addSubDestPackage("entity");
        beanCodemaConfig.setJavaClassNameParser(JavaClassNameParserFactory.fromSuffix("Entity"));

        //dao & xml mapper & dal config & mybatis xml config
        MybatisCodemaConfig mybatis = java.copy(MybatisCodemaConfig.class);
        mybatis.setMapperDir("mapper");
        mybatis.setJavaClassNameParser(JavaClassNameParserFactory.sourceSuffix("Dao"));
        mybatis.setFromPackage(beanCodemaConfig.getDestPackage());
        mybatis.setConfigPackage(mybatis.relativePackage("config"));
        mybatis.addSubDestPackage("dao");
        mybatis.setMapperXmlTemplates(Lists.newArrayList(new ClassPathResource("article.xml")));

        Codema.sourceLoader(new JavaDbSourceParser("jdbc:mysql://localhost:3306/lvbby?characterEncoding=UTF-8","root","","article"))
                .bind(mavenConfig)
                .bind(beanCodemaConfig)
                .bind(mybatis)
                .run();
    }
}
