package com.lvbby.codema.java;

import com.google.common.collect.Lists;
import com.lvbby.codema.app.bean.JavaBeanCodemaConfig;
import com.lvbby.codema.app.convert.JavaConvertCodemaConfig;
import com.lvbby.codema.app.testcase.JavaTestcaseCodemaConfig;
import com.lvbby.codema.app.testcase.mock.JavaMockTestCodemaConfig;
import com.lvbby.codema.app.testcase.mock.JavaMockTestCodemaMachine;
import com.lvbby.codema.core.Codema;
import com.lvbby.codema.core.CodemaGlobalContext;
import com.lvbby.codema.core.CodemaGlobalContextKey;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.handler.PrintResultHandler;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.mock.ServiceImpl;
import com.lvbby.codema.java.result.JavaRegisterResultHandler;
import com.lvbby.codema.java.source.JavaClassSourceParser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Created by dushang.lp on 2017/6/26.
 */
public class CodemaMachineTest extends BaseTest {

    @Before
    public void init() {
        File f = new File(JavaMockTestCodemaMachine.class.getResource("/").getPath());
        f = f.getParentFile().getParentFile();//codema-app
        f = f.getParentFile();//codema

        CodemaGlobalContext.set(CodemaGlobalContextKey.directoryRoot, Lists.newArrayList(f.getAbsolutePath()));
    }

    private <T extends JavaBasicCodemaConfig> T _newConfig(Class<T> clz) throws Exception {
        T config = clz.newInstance();
        config.setFrom(JavaClassSourceParser.toURI(ServiceImpl.class));
        config.setResultHandler(Lists.newArrayList(JavaRegisterResultHandler.class.getName(), PrintResultHandler.class.getName()));
        config.setDestPackage("com.lvbby");
        return config;
    }

    private void exec(CommonCodemaConfig config) throws Exception {
        Codema.exec(config);
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
    public void convert() throws Exception {
        JavaConvertCodemaConfig config = _newConfig(JavaConvertCodemaConfig.class);
        config.setConvertToClassName("BuildUtils");
        exec(config);
    }
}
