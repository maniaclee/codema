package com.lvbby.codema.java;

import com.lvbby.codema.app.bean.JavaBeanMachine;
import com.lvbby.codema.app.convert.JavaMapStructConvertMachine;
import com.lvbby.codema.app.mvn.MavenMachine;
import com.lvbby.codema.app.mybatis.MybatisMachine;
import com.lvbby.codema.app.repository.JavaRepositoryMachine;
import com.lvbby.codema.core.Machine;
import com.lvbby.codema.core.handler.ResultHandlerFactory;
import com.lvbby.codema.core.resource.ClassPathResource;
import com.lvbby.codema.core.tool.mysql.SqlMachineFactory;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.java.api.JavaSourceMachineFactory;
import com.lvbby.codema.java.baisc.JavaClassNameParserFactory;
import org.junit.Test;

import java.io.File;

/**
 *
 * @author dushang.lp
 * @version $Id: MybatisTest.java, v 0.1 2018年03月09日 下午12:48 dushang.lp Exp $
 */
public class MybatisTest extends BaseTest {
    @Test
    public void mybatis() throws Exception {
        MavenMachine maven = new MavenMachine();

        maven.setDestRootDir("~/workspace/");
        maven.setName("test-codema");
        maven.setGroupId("lvbby");
        maven.setArtifactId(maven.getName());
        maven.initMaven();

        for (Machine<SqlTable, SqlTable> sql : SqlMachineFactory.fromJdbcUrl("jdbc:mysql://localhost:3306/lvbby?characterEncoding=UTF-8",
            "root", "", "article")) {
            //entity
            JavaBeanMachine bean = new JavaBeanMachine();
            bean.javaClassNameParser(JavaClassNameParserFactory.suffix("Entity"))
                    .destPackage("com.lvbby.mybatis.entity")
                .destRootDir(maven.getDestSrcRoot());

            JavaBeanMachine dto = new JavaBeanMachine();
            dto.javaClassNameParser(JavaClassNameParserFactory.suffix("DTO"))
                    .destPackage("com.lvbby.mybatis.dto")
                .destRootDir(maven.getDestSrcRoot());

            JavaMapStructConvertMachine convert = new JavaMapStructConvertMachine();

            //dao & xml mapper & dal config & mybatis xml config
            MybatisMachine mybatis = new MybatisMachine();
            mybatis.setDestRootDir(maven.getDestSrcRoot());
            mybatis.setMapperName(sqlTable -> String.format("com.lvbby.mybatis.mapper.%sDao", sqlTable.getName()));
            mybatis.setMapperDir(new File(maven.getDestResourceRoot(), "mapper").getAbsolutePath());
            mybatis.setEntityMachine(bean);
            mybatis.setTemplateFunction(sqlTable -> new ClassPathResource(String.format("%s.xml", sqlTable.getName())));


            JavaRepositoryMachine repo = new JavaRepositoryMachine();
            repo.javaClassNameParser(source -> source.getName()+"Repo");
            repo.setDestPackage("com.lvbby.mybatis.repo");
            repo.setDestRootDir(maven.getDestSrcRoot());
            sql
                .next(JavaSourceMachineFactory.fromSqlTable().next(bean).next(dto).next(repo))
                .nextWithCheck(mybatis)
                .addResultHandler(ResultHandlerFactory.print)
//                .addResultHandler(ResultHandlerFactory.fileWrite)
//                .addResultHandler(result -> System.err.println(((FileResult) result).getFile()))
                .run();
        }
    }
}