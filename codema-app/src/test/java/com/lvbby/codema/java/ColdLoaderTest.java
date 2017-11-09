package com.lvbby.codema.java;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.lvbby.codema.app.testcase.JavaTestcaseCodemaMachine;
import com.lvbby.codema.app.testcase.mock.JavaMockTestCodemaMachine;
import com.lvbby.codema.app.testcase.mock.MockMethod;
import com.lvbby.codema.core.Codema;
import com.lvbby.codema.core.CodemaGlobalContext;
import com.lvbby.codema.core.CodemaGlobalContextKey;
import com.lvbby.codema.core.CodemaMachine;
import com.lvbby.codema.core.handler.PrintResultHandler;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.mock.ServiceImpl;
import com.lvbby.codema.java.result.JavaRegisterResultHandler;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
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
        f = f.getParentFile();//codema

        CodemaGlobalContext.set(CodemaGlobalContextKey.directoryRoot, Lists.newArrayList(f.getAbsolutePath()));
    }



    private <T extends JavaBasicCodemaConfig> T _newConfig(Class<T> clz) throws Exception {
        T config = clz.newInstance();
        config.setAuthor("maniaclee");
//        config.setFrom(JavaClassSourceParser.toURI(ServiceImpl.class));
        config.setResultHandlers(Lists.newArrayList(new JavaRegisterResultHandler(), new PrintResultHandler()));
        config.setDestPackage("com.lvbby");
        return config;
    }


    @Test
    public void mockUnitTest() throws Exception {
        List<MockMethod> mockMethods = MockMethod.parseMockMethods(JavaClass.from(Codema.class), javaField -> javaField.getType().beOutterClass());
        mockMethods.forEach(mockMethod -> {
            mockMethod.parseMockSentence().forEach(mockDependencyMethod -> {
                System.out.println(mockDependencyMethod);
            });
        });
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
    public void anno() throws Exception {
        System.out.println(CodemaMachine.class.isAssignableFrom(JavaTestcaseCodemaMachine.class));
        Field field = ServiceImpl.class.getDeclaredField("textHolder");
        Stream.of(field.getAnnotations()).forEach(annotation -> {
            System.out.println(annotation.annotationType().getName());
        });
    }
}
