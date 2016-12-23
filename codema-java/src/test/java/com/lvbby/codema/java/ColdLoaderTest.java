package com.lvbby.codema.java;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lvbby.codema.core.Codema;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

/**
 * Created by lipeng on 16/12/22.
 */
public class ColdLoaderTest {


    private void print(Object a) {
        System.out.println(JSON.toJSONString(a, SerializerFeature.PrettyFormat));
    }

    @Test
    public void snakeYamel() throws Exception {
        Yaml yaml = new Yaml();
        File f = new File("/Users/psyco/workspace/github/codema/src/main/resources/codema.yml");
        Iterable<Object> result = yaml.loadAll(new FileInputStream(f));
        for (Object obj : result) {
            System.out.println(obj.getClass());
            System.out.println(obj);
        }
        Map next = (Map) yaml.loadAll(new FileInputStream(f)).iterator().next();
        for (Object key : next.keySet()) {
            System.out.println(key + "   " + next.get(key).getClass());
            System.out.println(next.get(key));
        }
    }

    @Test
    public void codema() throws Exception {
        Codema.fromYaml(IOUtils.toString(ColdLoaderTest.class.getClassLoader().getResourceAsStream("codema.yml"))).run();
    }


}