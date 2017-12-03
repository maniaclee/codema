package com.lvbby.codema.java;

import com.google.common.collect.Lists;
import com.lvbby.codema.app.bean.JavaBeanMachine;
import com.lvbby.codema.app.mvn.MavenMachine;
import com.lvbby.codema.app.mybatis.MybatisMachine;
import com.lvbby.codema.core.Machine;
import com.lvbby.codema.core.handler.ResultHandlerFactory;
import com.lvbby.codema.core.resource.ClassPathResource;
import com.lvbby.codema.core.tool.mysql.SqlMachineFactory;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.java.baisc.JavaClassNameParserFactory;
import com.lvbby.codema.java.machine.JavaClassMachineFactory;
import com.lvbby.codema.java.tool.JavaSrcLoader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Created by dushang.lp on 2017/6/26.
 */
public class MybatisNewTest extends BaseTest {

    @Before public void init() {
        JavaSrcLoader.initJavaSrcRoots(
                Lists.newArrayList(new File(System.getProperty("user.home"), "workspace")));
    }

    @Test public void mybatis() throws Exception {
        MavenMachine maven = new MavenMachine();

        maven.setDestRootDir("~/workspace/");
        maven.setName("test-codema");
        maven.setGroupId("lvbby");
        maven.setArtifactId(maven.getName());
        maven.initMaven();

        //        JavaTestcaseCodemaConfig testcaseCodemaConfig = java.copy(JavaTestcaseCodemaConfig.class);
        //        testcaseCodemaConfig.setSpring(true);
        //        testcaseCodemaConfig.setFromPackage(mybatis.getDestPackage());
        //        testcaseCodemaConfig.setDestPackage("com.lvbby.codema.test.mybatis");
        //        testcaseCodemaConfig.setDestSrcRoot(maven.getDestTestSrcRoot());
        for (Machine<SqlTable, SqlTable> sql : SqlMachineFactory
                .fromJdbcUrl("jdbc:mysql://localhost:3306/lvbby?characterEncoding=UTF-8", "root",
                        "", "article")) {
            //entity
            JavaBeanMachine bean = new JavaBeanMachine();
            bean.javaClassNameParser(JavaClassNameParserFactory.suffix("Entity"))
                    .destPackage("com.lvbby.mybatis.entity")
                    .destRootDir(maven.getDestSrcRoot());

            //dao & xml mapper & dal config & mybatis xml config
            MybatisMachine mybatis = new MybatisMachine();
            mybatis.setDestRootDir(maven.getDestSrcRoot());
            mybatis.setConfigPackage("com.lvbby.mybatis.config");
            mybatis.setMapperDir(new File(maven.getDestResourceRoot(),"mapper").getAbsolutePath());
            mybatis.setJavaClassNameParser(JavaClassNameParserFactory.suffix("Dao"));
            //        mybatis.setConfigPackage(mybatis.relativePackage("config"));
            mybatis.setDestPackage("com.lvbby.mybatis.dao");
            mybatis.setJavaBeanClassFullName(bean.getDestJavaClassFullNameFuture());
            mybatis.setMapperXmlTemplate(
                    new ClassPathResource(String.format("%s.xml", sql.getSource().getName())));

            sql.resultHandlers(Lists.newArrayList(ResultHandlerFactory.printResultHandler,ResultHandlerFactory.fileResultHandler))
                    .nextWithCheck(JavaClassMachineFactory.fromSqlTable().nextWithCheck(bean))
                    .nextWithCheck(mybatis).code();
        }
    }
}
