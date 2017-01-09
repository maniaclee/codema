package com.lvbby.codema.core.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    public static Map<String, Object> object2map(Object object) throws Exception {
        Map<String, Object> re = Maps.newHashMap();
        for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(object.getClass(), Object.class).getPropertyDescriptors()) {
            if (propertyDescriptor.getReadMethod() != null)
                re.put(propertyDescriptor.getName(), propertyDescriptor.getReadMethod().invoke(object));
        }
        return re;
    }

    public static Object getProperty(Object b, String propertyName) {
        try {
            for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(b.getClass(), Object.class).getPropertyDescriptors()) {
                if (propertyDescriptor.getName().equals(propertyName)) {
                    return propertyDescriptor.getReadMethod().invoke(b);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String camel(String s, String... ss) {
        if (ss == null || ss.length == 0)
            return StringUtils.uncapitalize(s);
        return s.toLowerCase() + Lists.newArrayList(ss).stream().map(e -> StringUtils.capitalize(e)).collect(Collectors.joining());
    }

    public static String replace(String s, String regx, Function<Matcher, String> function) {
        StringBuilder re = new StringBuilder();
        int last = 0;
        Matcher matcher = Pattern.compile(regx).matcher(s);
        while (matcher.find()) {
            re.append(s.substring(last, matcher.start()));
            re.append(function.apply(matcher));
            last = matcher.end();
        }
        re.append(s.substring(last));
        return re.toString();
    }

    public static String findFirst(String src, String regx, Function<Matcher, String> function) {
        for (Matcher matcher = Pattern.compile(regx).matcher(src); matcher.find(); )
            return function.apply(matcher);
        return null;
    }

    public static Field getField(Class clz, String property) {
        try {
            return clz.getDeclaredField(property);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

}
