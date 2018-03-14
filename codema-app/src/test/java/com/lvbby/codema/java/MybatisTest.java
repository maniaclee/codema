package com.lvbby.codema.java;

import com.lvbby.codema.app.bean.JavaBeanMachine;
import com.lvbby.codema.app.convert.JavaMapStructConvertMachine;
import com.lvbby.codema.app.mybatis.MybatisMapperMachine;
import com.lvbby.codema.app.mybatis.MybatisMapperXmlMachine;
import com.lvbby.codema.app.repository.JavaRepositoryMachine;
import com.lvbby.codema.core.Machine;
import com.lvbby.codema.core.handler.ResultHandlerFactory;
import com.lvbby.codema.core.tool.mysql.SqlMachineFactory;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.java.api.JavaSourceMachineFactory;
import com.lvbby.codema.java.baisc.JavaClassNameParserFactory;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.tool.MavenConfig;
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
        MavenConfig maven = new MavenConfig();
        maven.setDestRootDir("~/workspace/test-codema");
        maven.setGroupId("lvbby");
        maven.setArtifactId("test-codema");

        for (Machine<SqlTable, SqlTable> sql : SqlMachineFactory.fromJdbcUrl("jdbc:mysql://localhost:3306/lvbby?characterEncoding=UTF-8",
            "root", "", "article")) {
            /** entity */
            Machine<JavaClass, JavaClass> bean = new JavaBeanMachine()
                    .javaClassNameParser(JavaClassNameParserFactory.format("com.lvbby.mybatis.entity.%sEntity"))
                    .destRootDir(maven.getDestSrcRoot());

            /** DTO */
            Machine<JavaClass, JavaClass> dto = new JavaBeanMachine()
                    .javaClassNameParser(JavaClassNameParserFactory.format("com.lvbby.mybatis.dto.%sDTO"))
                    .destRootDir(maven.getDestSrcRoot());

            /** build util */
            JavaMapStructConvertMachine convert = new JavaMapStructConvertMachine();
            convert.setConvertToClass(dto);
            convert.setDestRootDir(maven.getDestSrcRoot());
            convert.setDestClassName(JavaClassNameParserFactory.format("com.lvbby.mybatis.util.BuildUtil"));

            /** xml */
            MybatisMapperXmlMachine mybatisXml = new MybatisMapperXmlMachine();
            mybatisXml.setDestRootDir(maven.getDestResourceRoot());
            mybatisXml.setMapperName(sqlTable -> String.format("com.lvbby.mybatis.mapper.%sDao", sqlTable.getName()));
            mybatisXml.setMapperDir(new File(maven.getDestResourceRoot(), "mapper").getAbsolutePath());
            mybatisXml.setSqlTableMachine(sql);

            /** Dao mapper */
            MybatisMapperMachine mapper = new MybatisMapperMachine();
            mapper.setDestClassName(JavaClassNameParserFactory.format("com.lvbby.mybatis.mapper.%sDao"));
            mapper.setDestRootDir(maven.getDestSrcRoot());
            mapper.setMapperXmlMachine(mybatisXml);
            mapper.setSqlTableMachine(sql);

            /** repository */
            JavaRepositoryMachine repo = new JavaRepositoryMachine();
            repo.javaClassNameParser(JavaClassNameParserFactory.format("com.lvbby.mybatis.repo.%sRepo"));
            repo.setDestRootDir(maven.getDestSrcRoot());
            repo.setBuildClassMachine(convert);

            sql
                .next(JavaSourceMachineFactory.fromSqlTable()
                        .next(bean
                                .next(mybatisXml)
                                .next(mapper))
                        .next(dto)
                        .next(convert) )
                .addResultHandler(ResultHandlerFactory.print)
//                .addResultHandler(ResultHandlerFactory.fileWrite)
//                .addResultHandler(result -> System.err.println(((FileResult) result).getFile()))
                .run();
        }
    }
}