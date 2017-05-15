package com.lvbby.codema.java;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.lvbby.codema.app.testcase.JavaTestcaseCodemaConfig;
import com.lvbby.codema.core.Codema;
import com.lvbby.codema.core.SourceParser;
import com.lvbby.codema.core.config.DefaultConfigLoader;
import com.lvbby.codema.core.handler.PrintResultHandler;
import com.lvbby.codema.core.inject.CodemaInjector;
import com.lvbby.codema.java.result.JavaRegisterResultHandler;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.util.ServiceLoader;

/**
 * Created by lipeng on 16/12/22.
 */
public class ColdLoaderTest {

    private void print(Object a) {
        System.out.println(JSON.toJSONString(a, SerializerFeature.PrettyFormat));
    }


    @Test
    public void codemaYaml() throws Exception {
        Codema.fromYaml(IOUtils.toString(ColdLoaderTest.class.getClassLoader().getResourceAsStream("codema.yml"))).run();
    }

    @Test
    public void codemaApi() throws Exception {
        JavaTestcaseCodemaConfig config = new JavaTestcaseCodemaConfig();
        config.setAuthor("maniaclee");
        config.setSpring(true);
        config.setFrom("file://java:src///Users/dushang.lp/workspace/project/codema/codema-app/src/main/java/com/lvbby/codema/app/testcase/JavaTestcaseCodemaConfig.java");
        config.setResultHandler(Lists.newArrayList(JavaRegisterResultHandler.class.getName(), PrintResultHandler.class.getName()));
        config.setDestPackage("com.lvbby");
        Codema.from(new DefaultConfigLoader().addConfigRecursive(config)).run();
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
}
