package com.lvbby.codema.java;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.lvbby.codema.core.handler.PrintResultHandler;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.result.JavaRegisterResultHandler;

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

    protected  <T extends JavaBasicCodemaConfig> T _newConfig(Class<T> clz) throws Exception {
        T config = clz.newInstance();
        config.setResultHandlers(Lists.newArrayList(new JavaRegisterResultHandler(), new PrintResultHandler()));
        config.setDestPackage("com.lvbby");
        return config;
    }
}
