package com.lvbby.codema.java;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.lvbby.codema.app.testcase.JavaTestcaseCodemaConfig;
import com.lvbby.codema.app.testcase.mock.JavaMockTestCodemaConfig;
import com.lvbby.codema.app.testcase.mock.JavaMockTestCodemaMachine;
import com.lvbby.codema.core.Codema;
import com.lvbby.codema.core.CodemaGlobalContext;
import com.lvbby.codema.core.CodemaGlobalContextKey;
import com.lvbby.codema.core.SourceParser;
import com.lvbby.codema.core.handler.PrintResultHandler;
import com.lvbby.codema.core.inject.CodemaInjector;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.mock.ServiceImpl;
import com.lvbby.codema.java.result.JavaRegisterResultHandler;
import com.lvbby.codema.java.source.JavaClassSourceParser;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Stream;

/**
 * Created by lipeng on 16/12/22.
 */
public class ColdLoaderTest {

    private void print(Object a) {
        System.out.println(JSON.toJSONString(a, SerializerFeature.PrettyFormat));
    }


    @Before
    public void init() {
        File f = new File(JavaMockTestCodemaMachine.class.getResource("/").getPath());
        f = f.getParentFile().getParentFile();//codema-app
        f=f.getParentFile();//codema

        CodemaGlobalContext.set(CodemaGlobalContextKey.directoryRoot, Lists.newArrayList(f.getAbsolutePath()));
    }

    @Test
    public void codemaYaml() throws Exception {
        Codema.fromYaml(IOUtils.toString(ColdLoaderTest.class.getClassLoader().getResourceAsStream("codemaTest.yml"))).run();
    }

    @Test
    public void codemaApi() throws Exception {
        JavaTestcaseCodemaConfig config = new JavaTestcaseCodemaConfig();
        config.setAuthor("maniaclee");
        config.setSpring(true);
        //        config.setFrom("java://file//Users/dushang.lp/workspace/project/codema/codema-app/src/main/java/com/lvbby/codema/app/testcase/JavaTestcaseCodemaConfig.java");
        config.setFrom("java://file//Users/dushang.lp/workspace/project/codema/codema-app/src/main/java/com/lvbby/codema/app/mybatis/JavaMybatisCodemaConfig.java");
        config.setResultHandler(Lists.newArrayList(JavaRegisterResultHandler.class.getName(), PrintResultHandler.class.getName()));
        config.setDestPackage("com.lvbby");
        Codema.exec(config);
    }

    @Test
    public void templateEngineTest() throws Exception {
        JavaTestcaseCodemaConfig config = _newConfig(JavaTestcaseCodemaConfig.class);
        config.setSpring(true);
        Codema.exec(config);
    }

    private <T extends JavaBasicCodemaConfig> T _newConfig(Class<T> clz) throws Exception {
        T config = clz.newInstance();
        config.setAuthor("maniaclee");
        config.setFrom(JavaClassSourceParser.toURI(ServiceImpl.class));
        config.setResultHandler(Lists.newArrayList(JavaRegisterResultHandler.class.getName(), PrintResultHandler.class.getName()));
        config.setDestPackage("com.lvbby");
        return config;
    }

    @Test
    public void mockTest() throws Exception {
        JavaMockTestCodemaConfig config = _newConfig(JavaMockTestCodemaConfig.class);
        Codema.exec(config);
    }

    @Test
    public void mockUnitTest() throws Exception {
        List<JavaMockTestCodemaMachine.MockMethod> mockMethods = new JavaMockTestCodemaMachine().parseMockMethods(JavaClassSourceParser.toJavaClass(Codema.class));
        System.out.println(mockMethods);
    }

    @Test
    public void yaml() throws Exception {
        Yaml yaml = new Yaml();
        Iterable<Object> result = yaml.loadAll(ColdLoaderTest.class.getClassLoader().getResourceAsStream("codema.yml"));
        if (result == null || !result.iterator().hasNext())
            throw new IllegalArgumentException("no configuration found");
        for (Object o : result) {
            System.out.println(JSON.toJSONString(o, SerializerFeature.PrettyFormat));
        }
    }

    @Test
    public void name() throws Exception {
        Lists.newArrayList(ServiceLoader.load(SourceParser.class)).stream().forEach(e -> System.out.println(e.getClass().getName()));
        Lists.newArrayList(ServiceLoader.load(CodemaInjector.class)).stream().forEach(e -> System.out.println(e.getClass().getName()));
    }

    @Test
    public void anno() throws Exception {
        Field field = ServiceImpl.class.getDeclaredField("textHolder");
        Stream.of(field.getAnnotations()).forEach(annotation -> {
            System.out.println(annotation.annotationType().getName());
        });
    }
}
