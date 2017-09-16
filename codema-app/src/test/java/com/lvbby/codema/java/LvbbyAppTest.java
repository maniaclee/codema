/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.lvbby.codema.java;

import com.google.common.collect.Lists;
import com.lvbby.codema.app.bean.JavaBeanCodemaConfig;
import com.lvbby.codema.app.convert.JavaConvertCodemaConfig;
import com.lvbby.codema.app.delegate.JavaDelegateCodemaConfig;
import com.lvbby.codema.app.interfaces.JavaInterfaceCodemaConfig;
import com.lvbby.codema.app.mybatis.JavaMybatisCodemaConfig;
import com.lvbby.codema.app.repository.JavaRepositoryCodemaConfig;
import com.lvbby.codema.core.Codema;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.handler.FileWriterResultHandler;
import com.lvbby.codema.core.handler.PrintResultHandler;
import com.lvbby.codema.core.tool.mysql.UrlJdbcTableFactory;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.baisc.JavaClassNameParserFactory;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.result.JavaRegisterResultHandler;
import com.lvbby.codema.java.tool.JavaClassUtils;
import com.lvbby.codema.java.tool.JavaSrcLoader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 *
 * @author dushang.lp
 * @version $Id: LvbbyAppTest.java, v 0.1 2017-09-15 ����10:13 dushang.lp Exp $
 */
public class LvbbyAppTest extends BaseTest {

    @Before
    public void init() {
        JavaSrcLoader.initJavaSrcRoots(
            Lists.newArrayList(new File(System.getProperty("user.home"), "workspace")));
    }

    private JavaClass source() throws Exception {
        SqlTable comment = UrlJdbcTableFactory.of("jdbc:mysql://localhost:3306/lvbby?characterEncoding=UTF-8", "test", "12345").getTables().stream()
                .filter(table -> table.getNameInDb().equals("comment")).findAny().orElse(null);
        return JavaClassUtils.convert(comment);
    }
    @Test
    public void mybatis() throws Exception {

        CommonCodemaConfig config = new CommonCodemaConfig();
        config.setAuthor("lee");
        config.addResultHandler(PrintResultHandler.class)
            .addResultHandler(FileWriterResultHandler.class)
            .addResultHandler(JavaRegisterResultHandler.class);

        JavaBasicCodemaConfig java = config.copy(JavaBasicCodemaConfig.class);
        java.setDestPackage("com.lvbby.garfield");
        java.setDestRootDir("/Users/dushang.lp/workspace/project/lvbby-service/lvbby-service-biz/");
        java.setDestResourceRoot("/Users/dushang.lp/workspace/project/lvbby-service/lvbby-service-biz/src/main/resources");
        java.setDestSrcRoot("/Users/dushang.lp/workspace/project/lvbby-service/lvbby-service-biz/src/main/java");

        //entity
        JavaBeanCodemaConfig beanCodemaConfig = java.copy(JavaBeanCodemaConfig.class);
        beanCodemaConfig.addSubDestPackage("entity");
        beanCodemaConfig.setJavaClassNameParser(JavaClassNameParserFactory.fromSuffix("Entity"));

        //dto
        JavaBeanCodemaConfig dtoBeanCodemaConfig = java.copy(JavaBeanCodemaConfig.class);
        dtoBeanCodemaConfig.addSubDestPackage("api.dto");
        dtoBeanCodemaConfig.setDestSrcRoot("/Users/dushang.lp/workspace/project/lvbby-service/lvbby-service-api/src/main/java");
        dtoBeanCodemaConfig.setJavaClassNameParser(JavaClassNameParserFactory.fromSuffix("DTO"));

        //bean ---> DTO
        JavaConvertCodemaConfig convert = java.copy(JavaConvertCodemaConfig.class);
        convert.addSubDestPackage("utils");
        convert.setDestClassName("BuildUtils");
        convert.setFromPackage(beanCodemaConfig.getDestPackage());
        convert.setConvertToClassNameParser(JavaClassNameParserFactory.sourceSuffix("DTO"));//convert to DTO

        //dao & xml mapper & dal config & mybatis xml config
        JavaMybatisCodemaConfig mybatis = java.copy(JavaMybatisCodemaConfig.class);
        mybatis.setIdQuery(javaClass -> javaClass.getFields().stream()
            .filter(javaField -> javaField.getName().equals("nameCamel")).findAny().orElse(null));
        mybatis.setMapperDir("mapper");
        mybatis.setJavaClassNameParser(JavaClassNameParserFactory.sourceSuffix("Dao"));
        mybatis.setFromPackage(beanCodemaConfig.getDestPackage());
        mybatis.setConfigPackage(mybatis.relativePackage("config"));
        mybatis.addSubDestPackage("dao");

        JavaRepositoryCodemaConfig repo = java.copy(JavaRepositoryCodemaConfig.class);
        repo.addSubDestPackage("repo");
        repo.setFromPackage(mybatis.getDestPackage());
        repo.setConvertUtilsClass(convert.getDestClassName());
        repo.setJavaClassNameParser(JavaClassNameParserFactory.sourceSuffix("Repository"));

        JavaInterfaceCodemaConfig service = java.copy(JavaInterfaceCodemaConfig.class);
        service.setDestSrcRoot(dtoBeanCodemaConfig.getDestSrcRoot());
        service.setFromPackage(repo.getDestPackage());
        service.setJavaClassNameParser(JavaClassNameParserFactory.sourceSuffix("Service"));
        service.addSubDestPackage("api.service");

        JavaDelegateCodemaConfig serviceImpl = java.copy(JavaDelegateCodemaConfig.class);
        serviceImpl.setFromPackage(repo.getDestPackage());
        serviceImpl.setJavaClassNameParser(JavaClassNameParserFactory.sourceSuffix("ServiceImpl"));
        serviceImpl.addSubDestPackage("service");
        serviceImpl.addImplementInterface(service.getJavaClassNameParser());

        new Codema().withSource(source())
            .bind(beanCodemaConfig).bind(dtoBeanCodemaConfig).bind(convert).bind(mybatis).bind(repo)
            .bind(service).bind(serviceImpl).run();
    }

}