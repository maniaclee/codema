package com.lvbby.codema.java;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.File;

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

    public static File home() {
        return new File(System.getProperty("user.home"));
    }

    public static File myWorkspace() {
        String userName = System.getProperty("user.name");
        if (userName.startsWith("dushang")) {
            return new File(home(), "workspace/project");
        }
        return new File(home(), "workspace");
    }

    public static File workspace() {
        return new File(home(), "workspace");
    }

    public static String path(File file, String path) {
        return new File(file, path).getAbsolutePath();
    }
}
