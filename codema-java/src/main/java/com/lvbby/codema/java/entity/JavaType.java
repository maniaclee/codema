package com.lvbby.codema.java.entity;

import com.lvbby.codema.core.utils.ReflectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.*;

/**
 * Created by lipeng on 2017/1/10.
 */
public class JavaType {
    public static final JavaType VOID_TYPE = new JavaType();
    public static final String VOID = "void";
    private String name = VOID;
    private Type javaType;

    private JavaType() {
    }

    private JavaType(String name) {
        Validate.notBlank(name);
        this.name = name.trim();
    }

    private JavaType javaType(Type clz) {
        this.javaType = clz;
        return this;
    }


    public boolean bePrimitive() {
        if (beVoid())
            return false;
        char c = name.charAt(0);
        return c >= 'a' && c <= 'z';
    }

    public String getName() {
        return name;
    }

    public Class getJavaType() {
        if (javaType == null || beVoid())
            return null;
        if (isGenericType())
            return (Class) ((ParameterizedType) javaType).getRawType();
        return (Class) javaType;
    }


    public boolean beVoid() {
        return VOID.equalsIgnoreCase(name);
    }

    public boolean isGenericType() {
        if (javaType != null)
            return javaType instanceof ParameterizedType;
        return StringUtils.isNotBlank(name) && name.contains("<");
    }

    /***
     * 如果是泛型，返回泛型里的具体类
     * @return
     */
    public String getSpecificType() {
        if (!isGenericType())
            return name;
        return getGenericRealType();
    }

    public String getGenericRealType() {
        if (!isGenericType())
            return null;
        if (javaType != null)
            return ((ParameterizedType) javaType).getActualTypeArguments()[0].getTypeName();
        return ReflectionUtils.findFirst(name, "<([^\\[\\]]+)>", matcher -> matcher.group(1));
    }

    public static JavaType ofClassName(String className) {
        if (StringUtils.isBlank(className) || VOID.equalsIgnoreCase(className))
            return VOID_TYPE;
        JavaType javaType = new JavaType(ReflectionUtils.getSimpleClassName(className));
        javaType.javaType = tryGuessType(className);
        return javaType;
    }

    public static JavaType ofClass(Class clz) {
        if (clz == null)
            return VOID_TYPE;
        return new JavaType(clz.getSimpleName()).javaType(clz);
    }

    public static JavaType ofType(Type type) {
        if (type == null)
            return VOID_TYPE;
        if (type instanceof Class)
            return ofClass((Class) type);
        return new JavaType(type.getTypeName()).javaType(type);
    }

    public static JavaType ofMethodReturnType(Method method) {
        return ofType(ObjectUtils.firstNonNull(method.getGenericReturnType(), method.getReturnType()));
    }

    public static JavaType ofField(Field field) {
        return ofType(ObjectUtils.firstNonNull(field.getGenericType(), field.getType()));
    }

    public static JavaType ofMethodParameter(Parameter parameter) {
        return ofType(ObjectUtils.firstNonNull(parameter.getParameterizedType(), parameter.getType()));
    }

    private static Class<?> tryGuessType(String name) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JavaType javaType = (JavaType) o;

        return name != null ? name.equals(javaType.name) : javaType.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
