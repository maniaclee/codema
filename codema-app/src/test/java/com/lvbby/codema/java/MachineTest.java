package com.lvbby.codema.java;

import com.google.common.collect.Lists;
import com.lvbby.codema.app.bean.JavaBeanMachine;
import com.lvbby.codema.app.charset.CharsetMachine;
import com.lvbby.codema.app.convert.JavaConvertMachine;
import com.lvbby.codema.app.delegate.JavaDelegateMachine;
import com.lvbby.codema.app.interfaces.JavaInterfaceMachine;
import com.lvbby.codema.app.mvn.MavenMachine;
import com.lvbby.codema.app.mysql.MysqlInsertMachine;
import com.lvbby.codema.app.mysql.SqlCreateMachine;
import com.lvbby.codema.app.snippet.JavaRequestSettingMachine;
import com.lvbby.codema.app.testcase.JavaTestcaseMachine;
import com.lvbby.codema.app.testcase.mock.JavaMockTestMachine;
import com.lvbby.codema.core.Codema;
import com.lvbby.codema.core.Machine;
import com.lvbby.codema.core.bean.CodemaBean;
import com.lvbby.codema.core.handler.ResultHandlerFactory;
import com.lvbby.codema.core.machine.CommonMachineFactory;
import com.lvbby.codema.core.result.FileResult;
import com.lvbby.codema.core.tool.mysql.SqlMachineFactory;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.java.api.JavaSourceMachineFactory;
import com.lvbby.codema.java.baisc.JavaClassNameParserFactory;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaBaseMachine;
import com.lvbby.codema.java.machine.impl.JavaSimpleTemplateMachine;
import com.lvbby.codema.java.tool.JavaSrcLoader;
import com.lvbby.codema.java.tool.MavenConfig;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by dushang.lp on 2017/6/26.
 */
public class MachineTest extends BaseTest {

    static {

        File f = new File(System.getProperty("user.home"), "workspace");
        /** 设置java src 根路径*/
        JavaSrcLoader.initJavaSrcRoots(Lists.newArrayList(f));
    }

    private void exec(Machine machine) throws Exception {
        if(machine instanceof AbstractJavaBaseMachine){
            ((AbstractJavaBaseMachine) machine).setDestPackage("com.test");
        }
        Codema.execPrint(JavaSourceMachineFactory.fromClass().source(CodemaBean.class), machine);
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
        machine.setConvertToClassNameParser(JavaClassNameParserFactory.className("RepayPlanVO"));
        machine.setDestClassName(JavaClassNameParserFactory.className("BuildUtils"));
        Machine<String, JavaClass> source = JavaSourceMachineFactory.fromClassFullName()
            .source("com.lvbby.codema.java.tool.AutoImport");
        Codema.execPrint(source, machine);
    }

    @Test
    public void mapStructConvert() throws Exception {
//        JavaMapStructConvertMachine machine = new JavaMapStructConvertMachine();
//        machine.setDestPackage("com.lvbby.test.pack");
//        machine.setConvertToClassNameParser(JavaClassNameParserFactory.className("RepayPlanVO"));
//        machine.setDestClassName(JavaClassNameParserFactory.className("BuildUtils"));
//        Machine<String, JavaClass> source = JavaSourceMachineFactory.fromClassFullName().source(Machine.class.getName());
//        Codema.execPrint(source, machine);
    }

    @Test
    public void delegate() throws Exception {
        JavaDelegateMachine config = new JavaDelegateMachine();
        config.setDestClassName(JavaClassNameParserFactory.suffix("Impl"));
        config.setDetectInterface(true);
        config.setDestPackage("com.test");
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
        MavenConfig config = new MavenConfig();
        config.setGroupId("lvbby");
        config.setArtifactId("lvbby-maven");
        config.setDestRootDir("~/temp/lvbby-maven-project");
        new MavenMachine().source(config)
                .addResultHandler(ResultHandlerFactory.print)
                .addResultHandler(result -> System.out.println(((FileResult)result).getFile().getAbsolutePath()))
                .run();
    }

    @Test
    public void java2sqlCreate() throws Exception {
        String src = "@Data\n" + "@NoArgsConstructor\n" + "public class MachineEntity {\n" + "    //给前端的标识\n"
                     + "    private String          id;\n" + "    //展示文案\n" + "    private String          title;\n" + "    //描述\n"
                     + "    private String          description;\n" + "    //反序列化的方式\n" + "    private String type;\n"
                     + "    //machine的扩展参数\n" + "    private String          arg;\n" + "    /** 输入格式 */\n"
                     + "    private String          inFormat;\n" + "    /** 输出格式 */\n" + "    private String          outFormat;\n"
                     + "    /**\n" + "     * 序列化后的machine\n" + "     * @see MachineSerialConfig\n" + "     * */\n"
                     + "    private String          config;\n" + "\n" + "}";
        SqlCreateMachine sqlCreate = new SqlCreateMachine();
//        sqlCreate.setPrimaryKey("id");
        Codema.execPrint(JavaSourceMachineFactory.fromSrc().source(src).next(sqlCreate));
    }


    @Test
    public void mysql() throws Exception {
        //         Codema.source(JavaClassJdbcTableFactory.of(Bean.class).getTables().get(0))
        //            .bind(new MysqlSchemaCodemaConfig().addResultHandler(PrintResultHandler.class)).run();
    }

    @Test
    public void charset() throws Exception {
        String f = "/Users/dushang.lp/workspace/fintradecenter/app/core/model/src/main/java/com/alipay/fintradecenter/core/model/allowance/AssetAllowanceDetail.java";
        new CharsetMachine().source(new FileInputStream(f)).addResultHandler(ResultHandlerFactory.print).run();
    }

    /***
     * 读jdbc url
     * @throws Exception
     */
    @Test
    public void sql() throws Exception {
//        List<Machine<SqlTable, SqlTable>> machines = SqlMachineFactory
//            .fromJdbcUrl("jdbc:mysql://10.210.170.12:2883/zcbmodule?useUnicode=true", "obdv1:zcb0_721:root", "ali88", "fbc_trans_order");
//        //        List<Machine<SqlTable, SqlTable>> machines = SqlMachineFactory
//        //                .fromJdbcUrl("jdbc:mysql://localhost:3306/lvbby?characterEncoding=UTF-8", "root",
//        //                        "", "article");
//        for (Machine<SqlTable, SqlTable> source : machines) {
//            source.next(new MysqlInsertMachine()).next(new SqlSelectColumnsMachine()).next(new SqlUpdateMachine())
//                .addResultHandler(ResultHandlerFactory.print).run();
//        }
    }

    @Test
    public void sqlCreate() throws Exception {
        String s = "\n" + "\n" + " CREATE TABLE `finance_sequence_00` (\n" + "  `name` varchar(64) NOT NULL COMMENT 'sequence名称',\n"
                   + "  `gmt_create` timestamp NOT NULL COMMENT '创建时间',\n" + "  `gmt_modified` timestamp NOT NULL COMMENT '修改时间',\n"
                   + "  `value` int(11) NOT NULL COMMENT '序列值',\n" + "  `max_value` int(11) NOT NULL COMMENT '最大值',\n"
                   + "  `min_value` int(11) NOT NULL COMMENT '最小值',\n" + "  `step` int(11) NOT NULL COMMENT '步长',\n"
                   + "  PRIMARY KEY (`name`)\n" + ") \n" + "CREATE TABLE `finance_sequence_xxxx` (\n"
                   + "  `name` varchar(64) NOT NULL COMMENT 'sequence名称',\n" + "  `gmt_create` timestamp NOT NULL COMMENT '创建时间',\n"
                   + "  `gmt_modified` timestamp NOT NULL COMMENT '修改时间',\n" + "  `value` int(11) NOT NULL COMMENT '序列值',\n"
                   + "  `max_value` int(11) NOT NULL COMMENT '最大值',\n" + "  `min_value` int(11) NOT NULL COMMENT '最小值',\n"
                   + "  `step` int(11) NOT NULL COMMENT '步长',\n" + "  PRIMARY KEY (`name`)\n" + ") ";
        Codema
            .execPrint(SqlMachineFactory.fromSqlCreate().source(s).next(JavaSourceMachineFactory.fromSqlTable()).next(new MysqlInsertMachine()));
    }

    @Test
    public void requestSetting() throws Exception {
        Codema.execPrint(JavaSourceMachineFactory.fromClass().source(SqlTable.class).next(new JavaRequestSettingMachine()));
    }

    @Test
    public void snippet() throws Exception {
        String template = IOUtils.toString(new FileInputStream(
            "/Users/dushang.lp/workspace/project/codema/codema-app/src/main/java/com/lvbby/codema/app/snippet/RequestSetting"));
        JavaSimpleTemplateMachine next = new JavaSimpleTemplateMachine(template);

        Codema.execPrint(JavaSourceMachineFactory.fromClass().source(SqlTable.class), next);
    }

    /***
     * 生成builder
     * @throws Exception
     */
    @Test
    public void snippetBuilder() throws Exception {
        String template = IOUtils.toString(new FileInputStream(
            "/Users/dushang.lp/workspace/project/codema/codema-app/src/main/java/com/lvbby/codema/app/snippet/Builder"));
        JavaSimpleTemplateMachine next = new JavaSimpleTemplateMachine(template);

        Codema.execPrint(JavaSourceMachineFactory.fromClassFullName().source("com.lvbby.coder.MachineConfig"), next);
    }

    @Test
    public void sqlInsert2json() throws Exception {
        String source = "insert into `fbc_batch_41`( id,  batch_id,  request_biz_no,  biz_identity,  scenario,  product_id,  div_db_flag,  batch_status,  batch_num,  dispatch_num,  trigger_source,  execute_time,  biz_time,  gmt_create,  gmt_modified,  batch_type) values(1, '20180111009170000000410000000001', '20180111009190010000410000000001', 'p2p.jiebei', 'RAISE_PUSH', null, '201801110091700000004100', 'INITIAL', 1, 0, null, null, '2018-01-11 22:18:35', '2018-01-11 22:18:36', '2018-01-11 22:18:36', '');\n";
        Codema.execPrint(CommonMachineFactory.fromSqlInsert().source(source));
    }
    @Test
    public void sqlInsert2yaml() throws Exception {
        String source = "insert into `fbc_batch_41`( id,  batch_id,  request_biz_no,  biz_identity,  scenario,  product_id,  div_db_flag,  batch_status,  batch_num,  dispatch_num,  trigger_source,  execute_time,  biz_time,  gmt_create,  gmt_modified,  batch_type) values(1, '20180111009170000000410000000001', '20180111009190010000410000000001', 'p2p.jiebei', 'RAISE_PUSH', null, '201801110091700000004100', 'INITIAL', 1, 0, null, null, '2018-01-11 22:18:35', '2018-01-11 22:18:36', '2018-01-11 22:18:36', '');\n\n";
        Codema.execPrint(CommonMachineFactory.fromSqlInsert().source(source).next(CommonMachineFactory.toYaml()));
    }


    @Test
    public void yaml() throws Exception {
        String source = "insert into fin_user_loan_statement_41( statement_id,  gmt_create,  gmt_modified,  user_id,  statement_type,  disclose_date,  loan_demand_id,  product_id,  source_biz_no,  content,  uuid,  statement_source) values('20180117009190030000410000000398', '2018-01-17 22:38:27', '2018-01-17 22:38:27', '2088302233034419', 'DEMAND', '2018-01-17 22:38:27', '20180117009190010000410000000388', null, '201801171019301814000000000032000055555', '{\"profession\":\"SOCIAL_ORGANIZATION\",\"riskLevel\":\"低风险\",\"loanUsage\":\"消费\",\"loanOnOtherSource\":\"YES\",\"repaySource\":\"劳动所得\",\"repayGuarantee\":\"保证保险\",\"borrowerType\":\"INDIVIDUAL\",\"incomeInfo\":\"INCOME_0_5\",\"repayOverdueInfo\":\"YES\",\"debt\":\"DEBT_20_50\"}', '201801171019301814000000000032000055555', 'USER');\n";
        Codema.execPrint(CommonMachineFactory.fromSqlInsert().source(source).next(CommonMachineFactory.toYaml()));
    }
    @Test
    public void properties() throws Exception {
        String source = "#######默认配置\n" + "ENGINE=org.beetl.core.engine.FastRuntimeEngine\n" + "DELIMITER_PLACEHOLDER_START=${\n"
                        + "DELIMITER_PLACEHOLDER_END=}\n" + "DELIMITER_STATEMENT_START=<%\n" + "DELIMITER_STATEMENT_END=%>\n"
                        + "DIRECT_BYTE_OUTPUT = FALSE\n" + "HTML_TAG_SUPPORT = true\n" + "HTML_TAG_FLAG = #\n"
                        + "HTML_TAG_BINDING_ATTRIBUTE = var\n" + "NATIVE_CALL = TRUE\n" + "TEMPLATE_CHARSET = UTF-8\n"
                        + "ERROR_HANDLER = org.beetl.core.ConsoleErrorHandler\n"
                        + "NATIVE_SECUARTY_MANAGER= org.beetl.core.DefaultNativeSecurityManager\n"
                        + "RESOURCE_LOADER=org.beetl.core.resource.ClasspathResourceLoader\n" + "MVC_STRICT = FALSE";
        Codema.execPrint(CommonMachineFactory.fromProperties().source(source));
    }
    @Test
    public void properties2yaml() throws Exception {
        String source = "#######默认配置\n" + "ENGINE=org.beetl.core.engine.FastRuntimeEngine\n" + "DELIMITER_PLACEHOLDER_START=${\n"
                        + "DELIMITER_PLACEHOLDER_END=}\n" + "DELIMITER_STATEMENT_START=<%\n" + "DELIMITER_STATEMENT_END=%>\n"
                        + "DIRECT_BYTE_OUTPUT = FALSE\n" + "HTML_TAG_SUPPORT = true\n" + "HTML_TAG_FLAG = #\n"
                        + "HTML_TAG_BINDING_ATTRIBUTE = var\n" + "NATIVE_CALL = TRUE\n" + "TEMPLATE_CHARSET = UTF-8\n"
                        + "ERROR_HANDLER = org.beetl.core.ConsoleErrorHandler\n"
                        + "NATIVE_SECUARTY_MANAGER= org.beetl.core.DefaultNativeSecurityManager\n"
                        + "RESOURCE_LOADER=org.beetl.core.resource.ClasspathResourceLoader\n" + "MVC_STRICT = FALSE";
        Codema.execPrint(CommonMachineFactory.fromProperties().source(source).next(CommonMachineFactory.toYaml()));
    }
    @Test
    public void toProperties() throws Exception {
        String source = "insert into fin_user_loan_statement_41( statement_id,  gmt_create,  gmt_modified,  user_id,  statement_type,  disclose_date,  loan_demand_id,  product_id,  source_biz_no,  content,  uuid,  statement_source) values('20180117009190030000410000000398', '2018-01-17 22:38:27', '2018-01-17 22:38:27', '2088302233034419', 'DEMAND', '2018-01-17 22:38:27', '20180117009190010000410000000388', null, '201801171019301814000000000032000055555', '{\"profession\":\"SOCIAL_ORGANIZATION\",\"riskLevel\":\"低风险\",\"loanUsage\":\"消费\",\"loanOnOtherSource\":\"YES\",\"repaySource\":\"劳动所得\",\"repayGuarantee\":\"保证保险\",\"borrowerType\":\"INDIVIDUAL\",\"incomeInfo\":\"INCOME_0_5\",\"repayOverdueInfo\":\"YES\",\"debt\":\"DEBT_20_50\"}', '201801171019301814000000000032000055555', 'USER');\n";
        Codema.execPrint(CommonMachineFactory.fromSqlInsert().source(source).next(CommonMachineFactory.toProperties()));

    }

    @Test public void repo() throws Exception {

    }
}
