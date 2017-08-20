package com.lvbby.codema.java;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.Codema;
import com.lvbby.codema.core.CodemaGlobalContext;
import com.lvbby.codema.core.CodemaGlobalContextKey;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Created by dushang.lp on 2017/6/26.
 */
public class CodemaMachineAppTest extends BaseTest {

    @Before
    public void init() {
        File f = new File(System.getProperty("user.home"), "workspace");

        CodemaGlobalContext.set(CodemaGlobalContextKey.directoryRoot, Lists.newArrayList(f.getAbsolutePath()));
    }

    private void exec(CommonCodemaConfig config) throws Exception {
        Codema.exec(config);
    }

    @Test
    public void mock() throws Exception {
    }

}
