package com.lvbby.codema.core.utils;

import java.util.function.BinaryOperator;

/**
 * Created by lipeng on 2016/12/19.
 */
public class JavaUtils {

    /***
     * simple BinaryOperator , just return the reduce result
     */
    public static <T> BinaryOperator<T> binaryReturnOperator() {
        return (t, t2) -> t;
    }


    public static <T> T instance(Class<T> clz) {
        try {
            return clz.newInstance();
        } catch (Exception e) {
            return null;
        }
    }
}
