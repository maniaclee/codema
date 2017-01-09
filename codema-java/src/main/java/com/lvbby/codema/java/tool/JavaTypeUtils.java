package com.lvbby.codema.java.tool;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.utils.JavaUtils;

import java.lang.reflect.*;

/**
 * Created by lipeng on 17/1/9.
 */
public class JavaTypeUtils {

    public static String getClassType(Field field) {
        return _getClassType(field.getGenericType(), field.getType());
    }

    public static String getClassType(Parameter p) {
        return _getClassType(p.getParameterizedType(), p.getType());
    }

    public static String getClassType(Method m) {
        return _getClassType(m.getGenericReturnType(), m.getReturnType());
    }

    private static String _getClassType(Type... ps) {
        for (Type p : ps) {
            if (p instanceof ParameterizedType)
                return getParameterizedType(p);
            if (p instanceof Class)
                return ((Class) p).getSimpleName();
        }
        throw new IllegalArgumentException("type not found for types:" + Lists.newArrayList(ps));
    }

    private static String getParameterizedType(Type p) {
        return getTypeNameForShort(p.getTypeName());
    }

    private static String getTypeNameForShort(String s) {
        return JavaUtils.replace(s, "[^<>]+", m -> JavaUtils.findFirst(m.group(), "([^.]+\\.)*([^.]+)$", matcher -> matcher.group(2)));
    }
}
