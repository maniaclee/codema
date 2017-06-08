package com.lvbby.codema.core;

import java.util.List;

/**
 * Created by dushang.lp on 2017/6/8.
 */
public class CodemaGlobalContextKey<T> {

    public static final CodemaGlobalContextKey<List<String>> directoryRoot = new CodemaGlobalContextKey("directoryRoot");


    private String key;

    public CodemaGlobalContextKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
