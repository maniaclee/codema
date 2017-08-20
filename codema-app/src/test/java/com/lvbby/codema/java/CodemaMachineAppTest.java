package com.lvbby.codema.java;

import com.google.common.collect.Lists;
import com.lvbby.codema.app.mybatis.JavaMybatisCodemaConfig;
import com.lvbby.codema.core.Codema;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.java.source.JavaClassSourceParser;
import com.lvbby.codema.java.tool.JavaSrcLoader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Created by dushang.lp on 2017/6/26.
 */
public class CodemaMachineAppTest extends BaseTest {

    @Before
    public void init() {
        JavaSrcLoader.initJavaSrcRoots(Lists.newArrayList(new File(System.getProperty("user.home"), "workspace")));
    }

    private void exec(CommonCodemaConfig config) throws Exception {
        Codema.exec(config);
    }

    @Test
    public void mybatis() throws Exception {
        JavaMybatisCodemaConfig javaMybatisCodemaConfig = _newConfig(JavaMybatisCodemaConfig.class);
        javaMybatisCodemaConfig.setIdQuery(javaClass -> javaClass.getFields().stream().filter(javaField -> javaField.getName().equals("mapperDir")).findAny().orElse(null));

        new Codema()
                .source(JavaClassSourceParser.fromClass(JavaMybatisCodemaConfig.class))
                .bind(javaMybatisCodemaConfig)
                .run();
    }

}
