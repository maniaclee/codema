package com.lvbby.codema.java;

import com.google.common.collect.Lists;
import com.lvbby.codema.app.bean.JavaBeanMachine;
import com.lvbby.codema.app.mvn.MavenMachine;
import com.lvbby.codema.app.mybatis.MybatisOldMachine;
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
            //            entity
            JavaBeanMachine bean = new JavaBeanMachine();
            bean.javaClassNameParser(JavaClassNameParserFactory.suffix("Entity")).destPackage("com.lvbby.mybatis.entity")
                .destRootDir(maven.getDestSrcRoot());

            //dao & xml mapper & dal config & mybatis xml config
            MybatisOldMachine mybatis = new MybatisOldMachine();
            mybatis.setDestRootDir(maven.getDestSrcRoot());
            mybatis.setMapperName(sqlTable -> String.format("com.lvbby.mybatis.mapper.%sDao", sqlTable.getName()));
            mybatis.setMapperDir(new File(maven.getDestResourceRoot(), "mapper").getAbsolutePath());
            mybatis.setEntityName(sqlTable -> String.format("com.lvbby.mybatis.entity.%sEntity", sqlTable.getName()));
            mybatis.setTemplateFunction(sqlTable -> new ClassPathResource(String.format("%s.xml", sqlTable.getName())));

            sql.resultHandlers(Lists.newArrayList(ResultHandlerFactory.print))
                    .next(JavaSourceMachineFactory.fromSqlTable().next(bean))
                    .nextWithCheck(mybatis).run();
        }
    }
}