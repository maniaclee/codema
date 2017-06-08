package com.lvbby.codema.java.tool;

import com.github.javaparser.ast.CompilationUnit;
import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaGlobalContext;
import com.lvbby.codema.core.CodemaGlobalContextKey;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.entity.JavaClass;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dushang.lp on 2017/6/7.
 * java code 相关工具类
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
        put("boolean", "false");
        put("string", "\"\"");
        put("collection", "Lists.newArrayList()");
        put("list", "Lists.newArrayList()");
        put("set", "new HashSet()");
        put("map", "new HashMap()");
        put("bigdecimal", "BigDecimal.ZERO");
    }};


    public static CompilationUnit loadJavaSrcFromProject(Class clz) throws Exception {
        for (File file : getMavenSrcDirectories()) {
            File re = new File(file, clz.getName().replace('.', '/') + ".java");
            if (re.isFile() && re.exists()){
                return JavaLexer.read(re);
            }
        }
        return null;
    }

    public static List<File> getMavenSrcDirectories() {
        List<File> re = Lists.newArrayList();
        List<String> roots = CodemaGlobalContext.get(CodemaGlobalContextKey.directoryRoot);
        if (CollectionUtils.isNotEmpty(roots)) {
            for (String root : roots) {
                re.addAll(getMavenSrcDirectories(new File(root)));
            }
        }
        return re;
    }

    public static List<File> getMavenSrcDirectories(File file) {
        return getMavenRootDirectories(file).stream()
                .map(file1 -> new File(file1, "src/main/java"))
                .filter(file1 -> file1.isDirectory() && file1.exists())
                .collect(Collectors.toList());
    }

    public static List<File> getMavenRootDirectories(File file) {
        List<File> re = Lists.newArrayList();
        _getMavenSrcDirectories(file, re);
        return re;
    }

    static void _getMavenSrcDirectories(File file, List<File> result) {
        if (file == null || !file.exists() || !file.isDirectory())
            return;
        if (new File(file, "pom.xml").exists()) {
            result.add(file);
        }
        for (File child : file.listFiles()) {
            if (child.isDirectory())
                _getMavenSrcDirectories(child, result);
        }
    }

    public static boolean isOuterClass(Class clz) {
        return !clz.isPrimitive() && !clz.getPackage().getName().startsWith("java");
    }


    public static List<String> newObjectSentences(Class clz) {
        return newObjectSentences(clz, null);
    }

    /**
     * 基本类型因为类名没有表示度，所以没法自动命名
     * 返回类似：
     * <code>
     * JavaBasicCodemaConfig javaBasicCodemaConfig = new JavaBasicCodemaConfig();
     * javaBasicCodemaConfig.setAuthor("");
     * javaBasicCodemaConfig.setDestClassName("");
     * javaBasicCodemaConfig.setDestFile("");
     * </code>
     *
     * @param clz
     * @param varName
     * @return
     */
    public static List<String> newObjectSentences(Class clz, String varName) {
        if (!isOuterClass(clz)) {
            if (StringUtils.isBlank(varName))
                throw new IllegalArgumentException("not support basic class type: " + clz.getName());
            return Lists.newArrayList(newVar(clz.getSimpleName(), varName, newObjectValue(clz)));
        }

        List<String> result = Lists.newArrayList();

        Pair<String, String> re = newObjectSentence(clz);
        result.add(re.getRight());
        if (StringUtils.isBlank(varName))
            varName = re.getLeft();

        final String var = varName;
        if (isOuterClass(clz)) {
            ReflectionUtils.getFields(clz).forEach(field -> {
                String arg = null;
                if (isOuterClass(field.getPropertyType())) {
                    Pair<String, String> pair = newObjectSentence(field.getPropertyType());
                    result.add(pair.getRight());
                    arg = pair.getLeft();
                } else {
                    arg = newObjectValue(field.getPropertyType());
                }
                result.add(String.format("%s.%s(%s);", var, genSetter(field.getName()), arg));
            });
        }
        return result;
    }

    private static String newVar(String type, String var, String declare) {
        return String.format("%s %s = %s;", type, var, declare);
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
        return Pair.of(var, newVar(clz.getSimpleName(), var, newObjectValue(clz)));
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
        System.out.println(newObjectSentences(int.class, "s"));
        System.out.println(newObjectSentences(boolean.class, "s"));
        System.out.println(newObjectSentences(Boolean.class, "s"));
        System.out.println(newObjectSentences(String.class, "s"));
        System.out.println(newObjectSentences(JavaBasicCodemaConfig.class).stream().collect(Collectors.joining("\n")));

        System.out.println(getMavenSrcDirectories(new File("/Users/dushang.lp/workspace/zcbprod")).stream().map(File::toString).collect(Collectors.joining("\n")));
        System.out.println(getMavenRootDirectories(new File("/Users/dushang.lp/workspace/zcbprod")).stream().map(File::toString).collect(Collectors.joining("\n")));
        CodemaGlobalContext.set(CodemaGlobalContextKey.directoryRoot,Lists.newArrayList("/Users/dushang.lp/workspace/zcbprod"));
    }

}
