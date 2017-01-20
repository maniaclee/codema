package com.lvbby.codema.java;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.lvbby.codema.core.Codema;
import com.lvbby.codema.core.SourceParser;
import com.lvbby.codema.core.inject.CodemaInjector;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.util.ServiceLoader;

/**
 * Created by lipeng on 16/12/22.
 */
public class ColdLoaderTest {

    private void print(Object a) {
        System.out.println(JSON.toJSONString(a, SerializerFeature.PrettyFormat));
    }


    @Test
    public void codema() throws Exception {
        Codema.fromYaml(IOUtils.toString(ColdLoaderTest.class.getClassLoader().getResourceAsStream("codema.yml"))).run();
    }

    @Test
    public void name() throws Exception {
        Lists.newArrayList(ServiceLoader.load(SourceParser.class)).stream().forEach(e -> System.out.println(e.getClass().getName()));
        Lists.newArrayList(ServiceLoader.load(CodemaInjector.class)).stream().forEach(e -> System.out.println(e.getClass().getName()));
    }
}
