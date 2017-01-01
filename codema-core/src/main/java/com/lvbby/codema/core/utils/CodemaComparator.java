package com.lvbby.codema.core.utils;

import java.util.Comparator;

/**
 * Created by lipeng on 2017/1/1.
 */
public class CodemaComparator implements Comparator {

    public static Comparator instance = new CodemaComparator();

    @Override
    public int compare(Object o1, Object o2) {
        return getValue(o1).compareTo(getValue(o2));
    }

    private Integer getValue(Object a) {
        if (a != null) {
            if (a instanceof Ordered)
                return ((Ordered) a).getOrder();
            OrderValue annotation = a.getClass().getAnnotation(OrderValue.class);
            if (annotation != null)
                return annotation.value();
        }
        return Integer.MAX_VALUE;
    }
}
