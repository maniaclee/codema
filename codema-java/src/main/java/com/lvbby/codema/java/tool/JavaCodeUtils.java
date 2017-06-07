package com.lvbby.codema.java.tool;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.entity.JavaClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dushang.lp on 2017/6/7.
 */
public class JavaCodeUtils {

    public static List<String> numbers = Lists.newArrayList("int", "integer", "short", "double", "float", "byte", "long");
    public static List<String> basic = Lists.newArrayList("int", "integer", "short", "double", "float", "byte", "long", "boolean", "string");

    public static Map<String, String> typeMap = new HashMap() {{
        put("int", "0");
        put("integer", "0");
        put("double", "0");
        put("long", "0");
        put("short", "0");
        put("float", "0");
        put("byte", "0");
        put("boolean", "0");
        put("string", "\"\"");
        put("collection", "new ArrayList()");
        put("list", "new ArrayList()");
        put("set", "new HashSet()");
        put("map", "new HashMap()");
        put("bigdecimal", "BigDecimal.ZERO");
    }};

    public static boolean isOuterClass(Class clz) {
        return !clz.isPrimitive() && !clz.getPackage().getName().startsWith("java");
    }


    public static List<String> newObjectSentences(Class clz) {
        List<String> result = Lists.newArrayList();
        Pair<String, String> re = newObjectSentence(clz);
        result.add(re.getRight());
        if (isOuterClass(clz)) {
            ReflectionUtils.getFields(clz).forEach(field -> {
                System.err.println(field.getName());//TODO fixme
                Pair<String, String> pair = newObjectSentence(field.getPropertyType());
                result.add(pair.getRight());
                result.add(String.format("%s.%s(%s);", re.getLeft(), genSetter(field.getName()), pair.getLeft()));
            });
        }
        return result;
    }


    public static String genGetter(String s) {
        return String.format("get%s", StringUtils.capitalize(s));
    }

    public static String genSetter(String s) {
        return String.format("set%s", StringUtils.capitalize(s));
    }

    /***
     * left var name, right: sentence
     * @param clz
     * @return
     */
    private static Pair<String, String> newObjectSentence(Class clz) {
        String var = StringUtils.uncapitalize(clz.getSimpleName());
        return Pair.of(var, String.format("%s %s = %s;", clz.getSimpleName(), var, newObjectValue(clz)));
    }

    private static String newObjectValue(Class clz) {
        String s = typeMap.get(clz.getSimpleName().toLowerCase());
        if (s != null)
            return s;
        return String.format("new %s()", clz.getSimpleName());
    }

    private static String argName(Class clz) {
        return null;
    }

    public static void main(String[] args) {
        System.out.println(newObjectSentences(JavaClass.class));
        System.out.println(newObjectSentences(int.class));
        System.out.println(newObjectSentences(boolean.class));
        System.out.println(newObjectSentences(Boolean.class));
        System.out.println(newObjectSentences(String.class));
        System.out.println(newObjectSentences(JavaClass.class).stream().collect(Collectors.joining("\n")));
    }

}
