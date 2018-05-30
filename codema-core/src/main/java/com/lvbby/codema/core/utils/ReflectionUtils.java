package com.lvbby.codema.core.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.ClassPath;
import com.lvbby.codema.core.error.CodemaRuntimeException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.joor.Reflect;
import org.joor.ReflectException;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by lipeng on 2016/12/19.
 */
public class ReflectionUtils {

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
            throw new RuntimeException(e);
        }
    }

    public static <T> T instance(String clzName) {
        try {
            return instance((Class<T>) Class.forName(clzName));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
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

    /***
     * ToStringBuilder 转化为JSON
     * @return
     * @throws Exception
     */
    public static String apacheToStringBuilder2json(String s){
        /** 限定一个固定格式 */
        Validate.isTrue(s.matches("^[^\\[=]+\\[.*"));
        s = s.replaceAll("^[^\\[=]+\\[", "");
        Validate.isTrue(s.endsWith("]"), "invalid format");
        s = s.substring(0, s.length() - 1);

        /** 整体包装一下 */
        s = String.format("{%s}", s);

        Function<String,String> process = value -> {
            value = value.trim();
            if (value.matches("\\d+(\\.\\d+)?")) {
                return value;
            }
            if(StringUtils.equalsIgnoreCase("<null>",value)){
                return "null";
            }
            return String.format("'%s'", value);
        };
        /**处理value*/
        s = ReflectionUtils.replace(s, "=([^{}\\[\\],]+)([,}\\]])",
                matcher1 -> String.format("=%s%s", process.apply(matcher1.group(1)), matcher1.group(2)));
        /** 替换= */
        s = s.replaceAll("=", ":");
        return JSON.toJSONString(JSON.parseObject(s),true);
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

    public static List<String> findAll(String src, String regx, Function<Matcher, String> function) {
        List<String> re = Lists.newArrayList();
        for (Matcher matcher = Pattern.compile(regx).matcher(src); matcher.find(); )
            re.add(function.apply(matcher));
        return re;
    }

    public static <T> List<T> findAllConvert(String src, String regx, Function<Matcher, T> function) {
        List<T> re = Lists.newArrayList();
        for (Matcher matcher = Pattern.compile(regx).matcher(src); matcher.find(); ) {
            T apply = function.apply(matcher);
            if (apply != null)
                re.add(apply);
        }
        return re;
    }

    public static Field getField(Class clz, String property) {
        try {
            return clz.getDeclaredField(property);
        } catch (NoSuchFieldException e) {
            if (clz.getSuperclass() != null && !clz.getSuperclass().equals(Object.class))
                return getField(clz.getSuperclass(), property);
            return null;
        }
    }

    public static List<Class> loadClasses(String packageName) throws Exception {
        return loadClasses(packageName, null);
    }

    public static List<Class> loadClasses(String packageName, ClassLoader classLoader) throws Exception {
        if (classLoader == null)
            classLoader = ReflectionUtils.class.getClassLoader();
        //single class
        if (packageName.matches("(\\S+\\.)?[A-Z][^.]+$")) {
            return Lists.newArrayList(Class.forName(packageName));
        }
        //package,scan it
        return ClassPath.from(classLoader).getTopLevelClasses(packageName).stream()
                .map(ClassPath.ClassInfo::load)
                .filter(clz -> isSubClassOfObject(clz))
                .collect(Collectors.toList());
    }


    public static boolean isSubClassOfObject(Class clz) {
        while (clz.getSuperclass() != null)
            clz = clz.getSuperclass();
        return clz.equals(Object.class);
    }

    /**
     * copy other ==> dest,当dest的属性为空时，常用于继承父类的值
     *
     * @param dest  被copy的对象
     * @param other
     */
    public static void copyIfNull(Object dest, Object other) {
        if (dest == null || other == null)
            return;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(dest.getClass(), Object.class);
            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                String propertyName = propertyDescriptor.getName();
                //                //过滤引用类型
                if (!isValidPropertyToCopy(propertyDescriptor, dest))
                    continue;
                Object value = propertyDescriptor.getReadMethod().invoke(dest);
                Object otherValue = null;
                otherValue = getPropertyValue(other, propertyName);
                if (value == null && otherValue != null) {
                    propertyDescriptor.getWriteMethod().invoke(dest, otherValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isValidPropertyToCopy(PropertyDescriptor propertyDescriptor, Object arg) {
        try {
            if (!propertyDescriptor.getPropertyType().getName().startsWith("java"))
                return false;
            if (Collection.class.isAssignableFrom(propertyDescriptor.getPropertyType())) {
                ParameterizedType genericType = (ParameterizedType) ReflectionUtils.getField(arg.getClass(), propertyDescriptor.getName()).getGenericType();
                for (Type type : genericType.getActualTypeArguments()) {
                    if (type instanceof Class && !((Class) type).getName().startsWith("java"))
                        return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private static Object getPropertyValue(Object obj, String propertyName) {
        try {
            return Reflect.on(obj).field(propertyName).get();
        } catch (ReflectException e) {
            return null;
        }
        //        try {
        //            for (Field field : obj.getClass().getDeclaredFields()) {
        //                if (field.getName().equals(propertyName)) {
        //                    field.setAccessible(true);
        //                    return field.get(obj);
        //                }
        //            }
        //            /** apache's BeanUtils.getProperty 会把List<String> 转为String，妹的 */
        //            //            return BeanUtils.getProperty(obj, propertyName);
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
        //        return null;
    }

    public static <T> T copy(T obj, Class<T> clz) {
        try {
            T t = clz.newInstance();
            BeanUtils.copyProperties(t, obj);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.parseObject(JSON.toJSONString(obj), clz);
    }

    public static <K, V> Map<K, V> map(K k, V v) {
        Map<K, V> map = Maps.newHashMap();
        map.put(k, v);
        return map;
    }

    /***
     * java.util.List<java.lang.String>  ==> List<String>
     * @param className
     * @return
     */
    public static String getSimpleClassName(String className) {
        return className.replaceAll("[^.<>]+\\.", "");
    }

    public static String getPackage(String className) {
        if (!className.contains("."))
            return "";
        return className.replaceAll("(\\.[^.]+)$", "");
    }


    public static String loadResource(Class clz, String resourceName) throws IOException {
        return IOUtils.toString(clz.getResourceAsStream(resourceName));
    }


    public static <T> T getFieldWithAnnotation(Object obj, Class<? extends Annotation> anno) {
        Class<?> clz = obj.getClass();
        while (clz != null && !clz.equals(Object.class)) {
            for (Field field : clz.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(anno))
                    try {
                        return (T) field.get(obj);
                    } catch (Exception e) {
                        throw new CodemaRuntimeException("failed to getFieldWithAnnotation from " + ReflectionToStringBuilder.toString(obj));
                    }
            }
            clz = clz.getSuperclass();
        }
        return null;
    }

    public static String genGetter(String s) {
        return String.format("get%s", StringUtils.capitalize(s));
    }

    public static String genSetter(String s) {
        return String.format("set%s", StringUtils.capitalize(s));
    }

    public static List<PropertyDescriptor> getFields(Class obj) {
        return getFields(obj, propertyDescriptor -> propertyDescriptor.getReadMethod() != null && propertyDescriptor.getWriteMethod() != null);
    }

    public static List<Field> getAllFields(Class obj, Predicate<Field> predicate) {
        Field[] fields = obj.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
        }
        List<Field> re = Stream.of(fields).filter(field -> !Modifier.isStatic(field.getModifiers()) && (predicate == null || predicate.test(field))).collect(Collectors.toList());
        if (obj.getSuperclass() != null && !obj.getSuperclass().equals(Object.class))
            re.addAll(getAllFields(obj.getSuperclass(), predicate));
        return re;
    }

    public static boolean isJavaBeanProperty(Class<?> obj, Field field) {
        return getMethod(obj, genGetter(field.getName()), null) != null
                && getMethod(obj, genSetter(field.getName()), field.getType()) != null;
    }

    public static List<Field> getAllProperties(Class obj) {
        return getAllFields(obj, field -> isJavaBeanProperty(obj, field));
    }

    public static Method getMethod(Class clz, String method, Class<?>... types) {
        try {
            return clz.getMethod(method, types);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static List<Method> getAllMethods(Class<?> clz) {
        return getAllMethods(clz, null);
    }
    public static List<Method> getAllMethodsOfThisClass(Class<?> clz) {
        return Arrays.stream(clz.getDeclaredMethods()).filter(method -> Modifier.isPublic(method.getModifiers())
                && !Modifier.isStatic(method.getModifiers())).collect(Collectors.toList());
    }

    public static List<Method> getAllMethods(Class<?> clz, Predicate<Method> predicate) {
        Method[] methods = clz.getMethods();
        if (methods == null || methods.length == 0)
            return Lists.newLinkedList();
        List<Method> re = Stream.of(clz.getMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers())
                        && !Modifier.isStatic(method.getModifiers())
                        && !method.getDeclaringClass().equals(Object.class)
                        && (predicate == null || predicate.test(method)))
                .collect(Collectors.toList());
        return re;
    }

    public static List<PropertyDescriptor> getFields(Class obj, Predicate<PropertyDescriptor> predicate) {
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(obj);
        } catch (IntrospectionException e) {
            return Lists.newLinkedList();
        }
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        if (propertyDescriptors == null || propertyDescriptors.length == 0)
            return Lists.newLinkedList();
        return Lists.newArrayList(propertyDescriptors).stream().filter(predicate).collect(Collectors.toList());
    }

    public static List<Method> getMethodsWithAnnotation(Class<?> clz, Class<? extends Annotation> anno, boolean includeParent) {
        List<Method> re = Lists.newLinkedList();
        while (clz != null && !clz.equals(Object.class)) {
            for (Method m : clz.getMethods()) {
                m.setAccessible(true);
                if (m.isAnnotationPresent(anno))
                    try {
                        re.add(m);
                    } catch (Exception e) {
                        throw new CodemaRuntimeException("failed to getMethodsWithAnnotation from " + clz.getName());
                    }
            }
            if (!includeParent)
                return re;
            clz = clz.getSuperclass();
        }
        return re;
    }

    public static File makeSureDir(File file) {
        if (file.exists()) {
            Validate.isTrue(file.isDirectory(), String.format("%s is not directory", file));
        } else {
            Validate.isTrue(file.mkdirs(), String.format("%s can't be created", file));
        }
        return file;
    }


    public static <A extends Annotation> A getAnnotation(Class clz, Class<A> annotation) {
        A re = (A) clz.getAnnotation(annotation);
        if (re != null)
            return re;
        re = ClassUtils.getAllInterfaces(clz).stream().filter(aClass -> aClass.isAnnotationPresent(annotation)).findAny()
            .map(aClass -> aClass.getAnnotation(annotation)).orElse(null);
        if (re == null) {
            re = ClassUtils.getAllSuperclasses(clz).stream().filter(aClass -> aClass.isAnnotationPresent(annotation)).findAny()
                    .map(aClass -> aClass.getAnnotation(annotation)).orElse(null);
        }
        return re;
    }

    /***
     * 从方法或class上查找注解
     * @param method
     * @param annotation
     * @param <A>
     * @return
     */
    public static <A extends Annotation> A getAnnotationFromMethodOrClass(Method method, Class<A> annotation) {
        A re = method.getAnnotation(annotation);
        if(re!=null)
            return re;
        return getAnnotation(method.getDeclaringClass(),annotation);
    }
    /***
     * 是否是全类名(包含package)
     * @param s
     * @return
     */
    public static boolean isFullClassName(String s) {
        return s.contains(".");
    }

    public static Class<?> tryGuessType(String name) {
        try {
            return Class.forName(name);
        } catch (Exception e) {
        }
        String clzName = ReflectionUtils.getSimpleClassName(name);
        switch (clzName) {
            case "Integer":
                return Integer.class;
            case "int":
                return int.class;

            case "Byte":
                return Byte.class;
            case "byte":
                return byte.class;

            case "Long":
                return Long.class;
            case "long":
                return long.class;

            case "Float":
                return Float.class;
            case "float":
                return float.class;

            case "Double":
                return Double.class;
            case "double":
                return double.class;

            case "Short":
                return Short.class;
            case "short":
                return short.class;

            case "Character":
                return Character.class;
            case "char":
                return char.class;

            case "Boolean":
                return Boolean.class;
            case "boolean":
                return boolean.class;

            case "String":
                return String.class;
        }
        return null;
    }

}
