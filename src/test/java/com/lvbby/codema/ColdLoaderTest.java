package com.lvbby.codema;

import com.lvbby.codema.core.ConfigLoader;
import com.lvbby.codema.core.ConfigLoaderHelper;
import com.lvbby.codema.core.YamlConfigLoader;
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

    @Test
    public void sdf() throws Exception {
        ConfigLoader load = ConfigLoaderHelper.load(IOUtils.toString(new FileInputStream("/Users/psyco/workspace/github/codema/src/main/resources/codema.yml")), YamlConfigLoader.class);
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


}
