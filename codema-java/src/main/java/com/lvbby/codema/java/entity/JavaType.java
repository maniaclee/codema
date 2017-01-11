package com.lvbby.codema.java.entity;

import com.lvbby.codema.core.utils.ReflectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.*;

/**
 * Created by lipeng on 2017/1/10.
 */
public class JavaType {
    private String name;
    private Type javaType;

    private JavaType() {
    }

    private JavaType(String name) {
        this.name = name;
    }

    private JavaType javaType(Type clz) {
        this.javaType = clz;
        return this;
    }

    public String getName() {
        return name;
    }

    /***
     * 如果是void 返回void
     * @return
     */
    public String getNameDisplay() {
        return isVoid() ? "void" : name;
    }

    public boolean isVoid() {
        return StringUtils.isBlank(name);
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
        return ReflectionUtils.findFirst(name, "<([^[]]+)>", matcher -> matcher.group(1));
    }

    public static JavaType ofClassName(String className) {
        if (StringUtils.isBlank(className) || "void".equalsIgnoreCase(className))
            return new JavaType();
        return new JavaType(className.replaceAll("[^.]+\\.", ""));
    }

    public static JavaType ofClass(Class clz) {
        if (clz == null)
            return new JavaType();
        return new JavaType(clz.getSimpleName()).javaType(clz);
    }

    public static JavaType ofType(Type type) {
        if (type == null)
            return new JavaType();
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
