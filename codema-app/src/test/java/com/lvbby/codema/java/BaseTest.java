package com.lvbby.codema.java;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * Created by dushang.lp on 2017/6/26.
 */
public class BaseTest {
    protected void print(Object a) {
        System.out.println(JSON.toJSONString(a, SerializerFeature.PrettyFormat));
    }

    protected void println(String s) {
        System.out.println(s);
    }

}
