package com.lvbby.codema.java;

import com.google.common.collect.Lists;
import com.lvbby.codema.app.bean.JavaBeanCodemaConfig;
import com.lvbby.codema.app.convert.JavaConvertCodemaConfig;
import com.lvbby.codema.app.delegate.JavaDelegateCodemaConfig;
import com.lvbby.codema.app.interfaces.JavaInterfaceCodemaConfig;
import com.lvbby.codema.app.testcase.JavaTestcaseCodemaConfig;
import com.lvbby.codema.app.testcase.mock.JavaMockTestCodemaConfig;
import com.lvbby.codema.app.testcase.mock.JavaMockTestCodemaMachine;
import com.lvbby.codema.core.Codema;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.source.SourceLoader;
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


    private void exec(CommonCodemaConfig config) throws Exception {
        Codema.exec(config, sourceLoader);
    }

    private void exec(CommonCodemaConfig config, SourceLoader sourceLoader) throws Exception {
        Codema.exec(config, sourceLoader);
    }

    @Test
    public void mock() throws Exception {
        JavaMockTestCodemaConfig config = _newConfig(JavaMockTestCodemaConfig.class);
        exec(config);
    }

    @Test
    public void bean() throws Exception {
        JavaBeanCodemaConfig config = _newConfig(JavaBeanCodemaConfig.class);
        exec(config);
    }

    @Test
    public void testcase() throws Exception {
        JavaTestcaseCodemaConfig config = _newConfig(JavaTestcaseCodemaConfig.class);
        exec(config);
    }

    @Test
    public void delegate() throws Exception {
        JavaDelegateCodemaConfig config = _newConfig(JavaDelegateCodemaConfig.class);
        exec(config);
    }

    @Test
    public void convert() throws Exception {
        JavaConvertCodemaConfig config = _newConfig(JavaConvertCodemaConfig.class);
        config.setConvertToClassName("BuildUtils");
        exec(config);
    }

    @Test
    public void interfaces() throws Exception {
        JavaInterfaceCodemaConfig config = _newConfig(JavaInterfaceCodemaConfig.class);
        exec(config);
    }


}
