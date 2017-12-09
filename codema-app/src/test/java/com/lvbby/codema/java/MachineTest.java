package com.lvbby.codema.java;

import com.google.common.collect.Lists;
import com.lvbby.codema.app.bean.JavaBeanMachine;
import com.lvbby.codema.app.charset.CharsetMachine;
import com.lvbby.codema.app.convert.JavaConvertMachine;
import com.lvbby.codema.app.delegate.JavaDelegateMachine;
import com.lvbby.codema.app.interfaces.JavaInterfaceMachine;
import com.lvbby.codema.app.mvn.MavenMachine;
import com.lvbby.codema.app.mysql.MysqlSchemaMachine;
import com.lvbby.codema.app.testcase.JavaTestcaseMachine;
import com.lvbby.codema.app.testcase.mock.JavaMockTestMachine;
import com.lvbby.codema.core.Machine;
import com.lvbby.codema.core.bean.CodemaBean;
import com.lvbby.codema.core.handler.FileWriterResultHandler;
import com.lvbby.codema.core.handler.PrintResultHandler;
import com.lvbby.codema.core.handler.ResultHandlerFactory;
import com.lvbby.codema.java.baisc.JavaClassNameParserFactory;
import com.lvbby.codema.java.machine.JavaClassMachineFactory;
import com.lvbby.codema.java.source.JavaClassSourceParser;
import com.lvbby.codema.java.tool.JavaSrcLoader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by dushang.lp on 2017/6/26.
 */
public class MachineTest extends BaseTest {
    JavaClassSourceParser sourceLoader;

    @Before
    public void init() throws Exception {
        File f = new File(JavaMockTestMachine.class.getResource("/").getPath());
        f = f.getParentFile().getParentFile();//codema-app
        f = f.getParentFile();//codema

        /** 设置java src 根路径*/
        JavaSrcLoader.initJavaSrcRoots(Lists.newArrayList(f));
        try {
            sourceLoader = JavaClassSourceParser.fromClass(CodemaBean.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void exec(Machine machine) throws Exception {
        JavaClassMachineFactory.fromClass()
                .source(CodemaBean.class)
                .next(machine
                        .resultHandlers(Lists.newArrayList(new PrintResultHandler())))
                .run();
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
        JavaClassMachineFactory.fromClass().source(Machine.class).next(config).run();
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
        JavaClassMachineFactory.fromSrc()
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
}
