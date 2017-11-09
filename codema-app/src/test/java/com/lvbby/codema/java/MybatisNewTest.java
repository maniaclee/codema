package com.lvbby.codema.java;

import com.google.common.collect.Lists;
import com.lvbby.codema.app.bean.JavaBeanCodemaConfig;
import com.lvbby.codema.app.convert.JavaConvertCodemaConfig;
import com.lvbby.codema.app.delegate.JavaDelegateCodemaConfig;
import com.lvbby.codema.app.interfaces.JavaInterfaceCodemaConfig;
import com.lvbby.codema.app.mvn.MavenConfig;
import com.lvbby.codema.app.mybatis.JavaMybatisCodemaConfig;
import com.lvbby.codema.app.repository.JavaRepositoryCodemaConfig;
import com.lvbby.codema.core.Codema;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.handler.FileWriterResultHandler;
import com.lvbby.codema.core.handler.PrintResultHandler;
import com.lvbby.codema.core.tool.mysql.entity.SqlColumn;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.baisc.JavaClassNameParserFactory;
import com.lvbby.codema.java.result.JavaRegisterResultHandler;
import com.lvbby.codema.java.source.JavaClassSourceParser;
import com.lvbby.codema.java.tool.JavaSrcLoader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

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
        config.addResultHandler(PrintResultHandler.class)
                .addResultHandler(FileWriterResultHandler.class)
                .addResultHandler(JavaRegisterResultHandler.class)
        ;

        MavenConfig mavenConfig = config.copy(MavenConfig.class);
        mavenConfig.setDestRootDir("~/workspace/");
        mavenConfig.setName("test-codema");
        mavenConfig.setGroupId("lvbby");
        mavenConfig.setArtifactId(mavenConfig.getName());
        mavenConfig.initMaven();

        JavaBasicCodemaConfig java = config.copy(JavaBasicCodemaConfig.class);
        java.setDestPackage("com.lvbby.test.codema");
        java.setDestRootDir(mavenConfig.getDestSrcRoot());
        java.setDestResourceRoot(mavenConfig.getDestResourceRoot());
        java.setDestSrcRoot(mavenConfig.getDestSrcRoot());

        //entity
        JavaBeanCodemaConfig beanCodemaConfig = java.copy(JavaBeanCodemaConfig.class);
        beanCodemaConfig.addSubDestPackage("entity");
        beanCodemaConfig.setJavaClassNameParser(JavaClassNameParserFactory.fromSuffix("Entity"));

        //dto
        JavaBeanCodemaConfig dtoBeanCodemaConfig = java.copy(JavaBeanCodemaConfig.class);
        dtoBeanCodemaConfig.addSubDestPackage("dto");
        dtoBeanCodemaConfig.setJavaClassNameParser(JavaClassNameParserFactory.fromSuffix("DTO"));

        //bean ---> DTO
        JavaConvertCodemaConfig convert = java.copy(JavaConvertCodemaConfig.class);
        convert.addSubDestPackage("util");
        convert.setJavaClassNameParser(JavaClassNameParserFactory.className("BuildUtils"));
        convert.setFromPackage(beanCodemaConfig.getDestPackage());
        convert.setConvertToClassNameParser(JavaClassNameParserFactory.sourceSuffix("DTO"));//convert to DTO

        //dao & xml mapper & dal config & mybatis xml config
        JavaMybatisCodemaConfig mybatis = java.copy(JavaMybatisCodemaConfig.class);
        mybatis.setIdQuery(javaClass -> javaClass.getFields().stream().filter(javaField -> javaField.getName().equals("nameCamel")).findAny().orElse(null));
        mybatis.setMapperDir("mapper");
        mybatis.setJavaClassNameParser(JavaClassNameParserFactory.sourceSuffix("Dao"));
        mybatis.setFromPackage(beanCodemaConfig.getDestPackage());
        mybatis.setConfigPackage(mybatis.relativePackage("config"));
        mybatis.addSubDestPackage("dao");

        JavaRepositoryCodemaConfig repo = java.copy(JavaRepositoryCodemaConfig.class);
        repo.addSubDestPackage("repo");
        repo.setFromPackage(mybatis.getDestPackage());
        repo.setConvertUtilsClass(convert.getJavaClassNameParser());

        JavaInterfaceCodemaConfig service = java.copy(JavaInterfaceCodemaConfig.class);
        service.setFromPackage(repo.getDestPackage());
        service.setJavaClassNameParser(JavaClassNameParserFactory.sourceSuffix("Service"));
        service.addSubDestPackage("service");

        JavaDelegateCodemaConfig serviceImpl = java.copy(JavaDelegateCodemaConfig.class);
        serviceImpl.setFromPackage(repo.getDestPackage());
        serviceImpl.setJavaClassNameParser(JavaClassNameParserFactory.sourceSuffix("ServiceImpl"));
        serviceImpl.addSubDestPackage("service").addSubDestPackage("impl");
        serviceImpl.addImplementInterface(service.getJavaClassNameParser());

        Codema.sourceLoader(JavaClassSourceParser.fromClass(SqlColumn.class))
                .bind(mavenConfig)
                .bind(beanCodemaConfig)
                .bind(dtoBeanCodemaConfig)
                .bind(convert)
                .bind(mybatis)
                .bind(repo)
                .bind(service)
                .bind(serviceImpl)
                .run();
    }

}
