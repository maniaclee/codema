package com.lvbby.codema.java.tool;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.google.common.collect.Lists;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.entity.JavaArg;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaMethod;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dushang.lp on 2017/6/7.
 * java code related functions
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

    public static boolean isNumber(String className){
        return numbers.contains(className.toLowerCase());
    }


    private static boolean isEqual(JavaArg javaArg, Parameter parameter) {
        return parameter.getType().toString().equals(javaArg.getType().getName()) || parameter.getType().toString().equals(javaArg.getType().getFullName());
    }

    private static boolean isEqual(JavaMethod method, MethodDeclaration methodDeclaration) {
        if (!method.getName().equals(methodDeclaration.getNameAsString()))
            return false;
        NodeList<Parameter> parameters = methodDeclaration.getParameters();
        if (parameters.size() == method.getArgs().size()) {
            for (int i = 0; i < method.getArgs().size(); i++) {
                if (!isEqual(method.getArgs().get(i), parameters.get(i)))
                    return false;
            }
            return true;
        }
        return false;
    }


    public static JavaMethod findMethod(JavaClass javaClass, String methodName) {
        if (javaClass == null)
            return null;
        return javaClass.getMethods().stream().filter(method -> method.getName().equals(methodName)).findAny().orElse(null);
    }

    public static MethodDeclaration getMethodSrc(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, JavaMethod method) {
        if (classOrInterfaceDeclaration == null)
            return null;
        List<MethodDeclaration> methodsByName = classOrInterfaceDeclaration.getMethodsByName(method.getName());
        if (CollectionUtils.isEmpty(methodsByName))
            return null;
        if (methodsByName.size() == 1)
            return methodsByName.get(0);
        List<MethodDeclaration> collect = methodsByName.stream().filter(methodDeclaration -> isEqual(method, methodDeclaration)).collect(Collectors.toList());
        if (collect.isEmpty())
            return null;
        return collect.get(0);
    }


    public static boolean isOuterClass(Class clz) {
        return !clz.isPrimitive() && !clz.getPackage().getName().startsWith("java");
    }


    public static List<String> newObjectSentences(Class clz) {
        return newObjectSentences(clz, null);
    }

    /**
     * 结果类似
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


}
