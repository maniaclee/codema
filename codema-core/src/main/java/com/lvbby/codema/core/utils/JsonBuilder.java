package com.lvbby.codema.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by lipeng on 2016/12/4.
 */
public class JsonBuilder {
    private Map<String, Object> map = Maps.newHashMap();

    private JsonBuilder() {
    }


    public static JsonBuilder node() {
        return new JsonBuilder();
    }

    public JsonBuilder child(String key, Object jsonBuilder) {
        return child(true, key, jsonBuilder);
    }

    public JsonBuilder child(boolean condition, String key, Object jsonBuilder) {
        if (!condition)
            return this;
        if (jsonBuilder instanceof JsonBuilder)
            jsonBuilder = ((JsonBuilder) jsonBuilder).map;
        this.map.put(key, jsonBuilder);
        return this;
    }

    public String toJson() {
        return JSON.toJSONString(map, SerializerFeature.PrettyFormat);
    }


    @Override
    public String toString() {
        return toJson();
    }
}
