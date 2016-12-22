package com.lvbby.codema.core;

import com.alibaba.fastjson.JSON;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by lipeng on 16/12/22.
 */
public class YamlConfigLoader implements ConfigLoader {

    private Map<String, Object> cache = Maps.newConcurrentMap();


    @Override
    public void load(String code) throws Exception {

        YamlReader reader = new YamlReader(code);
        Object java = reader.get("java");
        System.out.println(JSON.toJSONString(java));
    }

    @Override
    public <T> T getConfig(Class<T> clz) {
        return null;
    }
}
