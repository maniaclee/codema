package com.lvbby.codema.java;

import com.google.common.collect.Lists;
import com.lvbby.codema.app.mvn.MavenConfig;
import com.lvbby.codema.app.mysql.MysqlSchemaCodemaConfig;
import com.lvbby.codema.app.testcase.JavaTestcaseCodemaMachine;
import com.lvbby.codema.app.testcase.mock.JavaMockTestCodemaConfig;
import com.lvbby.codema.app.testcase.mock.JavaMockTestCodemaMachine;
import com.lvbby.codema.core.Codema;
import com.lvbby.codema.core.CodemaMachine;
import com.lvbby.codema.core.handler.FileWriterResultHandler;
import com.lvbby.codema.core.handler.PrintResultHandler;
import com.lvbby.codema.core.tool.mysql.JavaClassJdbcTableFactory;
import com.lvbby.codema.java.entity.Bean;
import com.lvbby.codema.java.result.JavaRegisterResultHandler;
import com.lvbby.codema.java.source.JavaClassSourceParser;
import com.lvbby.codema.java.tool.JavaSrcLoader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Created by dushang.lp on 2017/6/26.
 */
public class CodemaMachineTest extends BaseTest {
    JavaClassSourceParser sourceLoader;

    @Before
    public void init() {
        File f = new File(JavaMockTestCodemaMachine.class.getResource("/").getPath());
        f = f.getParentFile().getParentFile();//codema-app
        f = f.getParentFile();//codema

        /** 设置java src 根路径*/
        JavaSrcLoader.initJavaSrcRoots(Lists.newArrayList(f));
        try {
            sourceLoader = JavaClassSourceParser.fromClass(JavaMockTestCodemaConfig.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exec(CodemaMachine machine) throws Exception {
        machine
                .resultHandlers(Lists.newArrayList(new PrintResultHandler()))
                .source(sourceLoader.loadSource().get(0))
                .code();
    }

    @Test
    public void mock() throws Exception {
        exec(new JavaMockTestCodemaMachine());
    }

    @Test
    public void bean() throws Exception {
//        exec(config);
    }

    @Test
    public void testcase() throws Exception {
        exec(new JavaTestcaseCodemaMachine());
    }

    @Test
    public void delegate() throws Exception {
//        JavaDelegateCodemaConfig config = _newConfig(JavaDelegateCodemaConfig.class);
//        config.setImplementInterfaces(Lists.newArrayList(
//                JavaClassNameParserFactory.className(Serializable.class.getSimpleName()),
//                JavaClassNameParserFactory.className("Test")
//                ));
//        exec(config);
    }

    @Test
    public void convert() throws Exception {
//        JavaConvertCodemaConfig config = _newConfig(JavaConvertCodemaConfig.class);
//        config.setConvertToClassNameParser(JavaClassNameParserFactory.className("RtSubjectQuestionRelVo"));
//        Codema.exec(config, JavaClassSourceParser.fromFile(new File("/Users/dushang.lp/workspace/finrtcenter/app/common/dal/src/main/java/com/alipay/finrtcenter/common/dal/zcbconfig/dataobject/RtSubjectQuestionRelDO.java")));
    }

    @Test
    public void interfaces() throws Exception {
//        JavaInterfaceCodemaConfig config = _newConfig(JavaInterfaceCodemaConfig.class);
//        exec(config);
    }

    @Test
    public void maven() throws Exception {
        MavenConfig config = new MavenConfig();
        config.setName("lvbby-maven-project");
        config.setGroupId("lvbby");
        config.setArtifactId("lvbby-maven");
        config.setDestRootDir("~/temp");
        config.setResultHandlers(
            Lists.newArrayList(new PrintResultHandler(), new FileWriterResultHandler()));
        exec(config);
    }

    @Test
    public void mysqlFromString() throws Exception {
        String src = "public   class TimeEvent {\n" + "\n" + "        private long id;\n"
                     + "        private long startDate;\n" + "        private long endDate;\n"
                     + "        //history,holiday\n" + "        private String type;\n"
                     + "        private String body;\n" + "        private String extra;}";
        MysqlSchemaCodemaConfig config = new MysqlSchemaCodemaConfig();
        config.setPrimaryKey("startDate");
        Codema.sourceLoader(JavaClassSourceParser.fromClassSrcString(src))
            .bind(config.addResultHandler(PrintResultHandler.class)).run();
    }
    @Test
    public void mysql() throws Exception {
         Codema.source(JavaClassJdbcTableFactory.of(Bean.class).getTables().get(0))
            .bind(new MysqlSchemaCodemaConfig().addResultHandler(PrintResultHandler.class)).run();
    }


}
