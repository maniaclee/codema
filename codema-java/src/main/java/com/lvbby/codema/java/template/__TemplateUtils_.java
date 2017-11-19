package com.lvbby.codema.java.template;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by lipeng on 2017/7/25.
 */
public class __TemplateUtils_ {

    /***
     *在java code里为模板引擎打的戳，让codema去翻译成模板语言
     * @param sentence
     * @return
     */
    public static boolean isTrue(String sentence) {
        return true;
    }


    public static List<String>  varList(String s ){
        return Lists.newArrayList();
    }

    public static boolean isFalse(String sentence) {
        return false;
    }

    public static void print(String s) {
    }

    public static void println(String s) {
    }
}
