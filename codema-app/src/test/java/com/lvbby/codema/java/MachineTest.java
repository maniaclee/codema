package com.lvbby.codema.java;

import com.google.common.collect.Lists;
import com.lvbby.codema.app.bean.JavaBeanMachine;
import com.lvbby.codema.app.charset.CharsetMachine;
import com.lvbby.codema.app.convert.JavaConvertMachine;
import com.lvbby.codema.app.delegate.JavaDelegateMachine;
import com.lvbby.codema.app.interfaces.JavaInterfaceMachine;
import com.lvbby.codema.app.mvn.MavenMachine;
import com.lvbby.codema.app.mysql.MysqlInsertMachine;
import com.lvbby.codema.app.mysql.MysqlSchemaMachine;
import com.lvbby.codema.app.mysql.SqlSelectColumnsMachine;
import com.lvbby.codema.app.mysql.SqlUpdateMachine;
import com.lvbby.codema.app.snippet.BasicJavaCodeMachine;
import com.lvbby.codema.app.testcase.JavaTestcaseMachine;
import com.lvbby.codema.app.testcase.mock.JavaMockTestMachine;
import com.lvbby.codema.core.Codema;
import com.lvbby.codema.core.Machine;
import com.lvbby.codema.core.bean.CodemaBean;
import com.lvbby.codema.core.handler.FileWriterResultHandler;
import com.lvbby.codema.core.handler.PrintResultHandler;
import com.lvbby.codema.core.handler.ResultHandlerFactory;
import com.lvbby.codema.core.tool.mysql.SqlMachineFactory;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.java.baisc.JavaClassNameParserFactory;
import com.lvbby.codema.java.machine.JavaSourceMachineFactory;
import com.lvbby.codema.java.tool.JavaSrcLoader;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * Created by dushang.lp on 2017/6/26.
 */
public class MachineTest extends BaseTest {

    @Before
    public void init() throws Exception {
//        File f = new File(JavaMockTestMachine.class.getResource("/").getPath());
//        f = f.getParentFile().getParentFile();//codema-app
//        f = f.getParentFile();//codema

        File f = new File(System.getProperty("user.home"),"workspace");
        /** 设置java src 根路径*/
        JavaSrcLoader.initJavaSrcRoots(Lists.newArrayList(f));
    }

    private void exec(Machine machine) throws Exception {
        Codema.exec(
                JavaSourceMachineFactory.fromClass().source(CodemaBean.class)
                ,machine);
    }

    @Test
    public void mock() throws Exception {
        exec(new JavaMockTestMachine());
    }

    @Test
    public void bean() throws Exception {
        exec(new JavaBeanMachine());
    }

    @Test
    public void testcase() throws Exception {
        JavaTestcaseMachine machine = new JavaTestcaseMachine();
        machine.setDestPackage("com.lvbby.test.pack");
        exec(machine);
    }


    @Test
    public void convert() throws Exception {
        JavaConvertMachine machine = new JavaConvertMachine();
        machine.setDestPackage("com.lvbby.test.pack");
        machine.setConvertToClassNameParser(JavaClassNameParserFactory.className("Shit"));
        exec(machine);
    }
    @Test
    public void delegate() throws Exception {
        JavaDelegateMachine config =  new JavaDelegateMachine();
        config.setJavaClassNameParser(JavaClassNameParserFactory.suffix("Impl"));
        config.setDetectInterface(true);
        JavaSourceMachineFactory.fromClass().source(Machine.class).next(config).run();
    }

    @Test
    public void interfaces() throws Exception {
        JavaInterfaceMachine machine = new JavaInterfaceMachine();
        machine.setDestPackage("com.lvbby.test.pack");
        exec(machine);
    }

    @Test
    public void maven() throws Exception {
        MavenMachine config = new MavenMachine();
        config.setName("lvbby-maven-project");
        config.setGroupId("lvbby");
        config.setArtifactId("lvbby-maven");
        config.setDestRootDir("~/temp");
        config.resultHandlers(
            Lists.newArrayList(new PrintResultHandler(), new FileWriterResultHandler()));
        config.doCode();
    }

    @Test
    public void mysqlFromString() throws Exception {
        String src = "public   class TimeEvent {\n" + "\n" + "        private long id;\n"
                     + "        private long startDate;\n" + "        private long endDate;\n"
                     + "        private String type;\n"
                     + "        private String body;\n" + "        private String extra;}";
        MysqlSchemaMachine sqlCreate = new MysqlSchemaMachine();
        sqlCreate.setPrimaryKey("startDate");
        JavaSourceMachineFactory.fromSrc()
                .source(src)
                .next(sqlCreate)
                .run();
    }
    @Test
    public void mysql() throws Exception {
//         Codema.source(JavaClassJdbcTableFactory.of(Bean.class).getTables().get(0))
//            .bind(new MysqlSchemaCodemaConfig().addResultHandler(PrintResultHandler.class)).run();
    }

    @Test public void charset() throws Exception {
        String f = "/Users/dushang.lp/workspace/fintradecenter/app/core/model/src/main/java/com/alipay/fintradecenter/core/model/allowance/AssetAllowanceDetail.java";
        new CharsetMachine().source(new FileInputStream(f))
                .addResultHandler(ResultHandlerFactory.print)
                .run();
    }

    /***
     * 读jdbc url
     * @throws Exception
     */
    @Test public void sql() throws Exception {
        List<Machine<SqlTable, SqlTable>> machines = SqlMachineFactory
                .fromJdbcUrl("jdbc:mysql://10.210.170.12:2883/zcbmodule?useUnicode=true", "obdv1:zcb0_721:root",
                        "ali88", "fbc_user_contract");
//        List<Machine<SqlTable, SqlTable>> machines = SqlMachineFactory
//                .fromJdbcUrl("jdbc:mysql://localhost:3306/lvbby?characterEncoding=UTF-8", "root",
//                        "", "article");
        for (Machine<SqlTable, SqlTable> source : machines) {
              source.next(new MysqlInsertMachine())
                    .next(new SqlSelectColumnsMachine())
                    .next(new SqlUpdateMachine())
                    .addResultHandler(ResultHandlerFactory.print)
                    .run();
        }
    }

    @Test public void sqlCreate() throws Exception {
        String s = "\n" + "\n" + " CREATE TABLE `finance_sequence_00` (\n"
                   + "  `name` varchar(64) NOT NULL COMMENT 'sequence名称',\n"
                   + "  `gmt_create` timestamp NOT NULL COMMENT '创建时间',\n"
                   + "  `gmt_modified` timestamp NOT NULL COMMENT '修改时间',\n"
                   + "  `value` int(11) NOT NULL COMMENT '序列值',\n"
                   + "  `max_value` int(11) NOT NULL COMMENT '最大值',\n"
                   + "  `min_value` int(11) NOT NULL COMMENT '最小值',\n"
                   + "  `step` int(11) NOT NULL COMMENT '步长',\n" + "  PRIMARY KEY (`name`)\n"
                   + ") \n" + "CREATE TABLE `finance_sequence_xxxx` (\n"
                   + "  `name` varchar(64) NOT NULL COMMENT 'sequence名称',\n"
                   + "  `gmt_create` timestamp NOT NULL COMMENT '创建时间',\n"
                   + "  `gmt_modified` timestamp NOT NULL COMMENT '修改时间',\n"
                   + "  `value` int(11) NOT NULL COMMENT '序列值',\n"
                   + "  `max_value` int(11) NOT NULL COMMENT '最大值',\n"
                   + "  `min_value` int(11) NOT NULL COMMENT '最小值',\n"
                   + "  `step` int(11) NOT NULL COMMENT '步长',\n" + "  PRIMARY KEY (`name`)\n" + ") ";
//        Codema.exec(SqlMachineFactory.fromSqlCreate().source(s),new MysqlInsertMachine());
        Codema.exec(SqlMachineFactory.fromSqlCreate().source(s).next(
                JavaSourceMachineFactory.fromSqlTable()));
    }

    @Test public void snippet() throws Exception {
        String template = IOUtils.toString(new FileInputStream(
                "/Users/dushang.lp/workspace/project/codema/codema-app/src/main/java/com/lvbby/codema/app/snippet/RequestSetting"));
        BasicJavaCodeMachine next = new BasicJavaCodeMachine(template);

        Codema.exec(JavaSourceMachineFactory.fromClass().source(SqlTable.class),next);
    }

    /***
     * 生成builder
     * @throws Exception
     */
    @Test public void snippetBuilder() throws Exception {
        String template = IOUtils.toString(new FileInputStream(
                "/Users/dushang.lp/workspace/project/codema/codema-app/src/main/java/com/lvbby/codema/app/snippet/Builder"));
        BasicJavaCodeMachine next = new BasicJavaCodeMachine(template);

        Codema.exec(JavaSourceMachineFactory.fromClassFullName().source("com.lvbby.coder.MachineConfig"),next);
    }

}
