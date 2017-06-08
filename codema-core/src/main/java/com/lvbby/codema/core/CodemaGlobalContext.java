package com.lvbby.codema.core;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by dushang.lp on 2017/6/8.
 */
public class CodemaGlobalContext {

    private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal() {
        @Override
        protected Object initialValue() {
            return Maps.newHashMap();
        }
    };

    public static <T> T get(String key) {
        return (T) threadLocal.get().get(key);
    }

    public static <T> T get(CodemaGlobalContextKey<T> key) {
        return (T) threadLocal.get().get(key.getKey());
    }


    public static void set(String key, Object obj) {
        threadLocal.get().put(key, obj);
    }

    public static <T> void set(CodemaGlobalContextKey<T> key, T obj) {
        threadLocal.get().put(key.getKey(), obj);
    }

    public static void clear() {
        threadLocal.remove();
    }
}
