package com.lvbby.codema.java;

import com.google.common.collect.Lists;
import com.lvbby.codema.app.bean.JavaBeanCodemaConfig;
import com.lvbby.codema.app.mvn.MavenConfig;
import com.lvbby.codema.app.mybatis.MybatisCodemaConfig;
import com.lvbby.codema.core.Codema;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.handler.FileWriterResultHandler;
import com.lvbby.codema.core.handler.PrintResultHandler;
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

        Codema.sourceLoader(new JavaDbSourceParser("jdbc:mysql://localhost:3306/test?characterEncoding=UTF-8","root","","rt_user_detail"))
                .bind(mavenConfig)
                .bind(beanCodemaConfig)
                .bind(mybatis)
                .run();
    }


    @Test
    public   void getConnAndTableStruct(){
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSetMetaData rsmd = null;
        try {
            //mysql连接
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/test", "root", "");
            //oracle连接
            //            Class.forName("oracle.jdbc.driver.OracleDriver");
            //            String url="jdbc:oracle:thin:@173.10.2.11:1521:test";
            //             connection=DriverManager.getConnection(url,"root","root");
            pstmt = (PreparedStatement) connection.prepareStatement("select * from rt_user_detail limit 1");
            pstmt.execute();  //这点特别要注意:如果是Oracle而对于mysql可以不用加.
            rsmd = (ResultSetMetaData) pstmt.getMetaData();
            for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                System.out.println(rsmd.getColumnName(i)+ "  " +rsmd.getColumnTypeName(i)
                                   +"  " +rsmd.getColumnClassName(i)+ "  "+rsmd.getTableName(i));
            }
        }
        catch ( Exception cnfex) {
            cnfex.printStackTrace();
        }

    }

}
