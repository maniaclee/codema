package com.lvbby.codema.java.entity;

import com.lvbby.codema.core.utils.ReflectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.*;

/**
 * Created by lipeng on 2017/1/10.
 */
public abstract class JavaType {
    public static final JavaType VOID_TYPE = new VoidClass();
    public static final String VOID = "void";

    public boolean bePrimitive() {
        String name = getName();
        return StringUtils.isNotBlank(name) && StringUtils.isAllLowerCase(name);
    }

    public abstract String getName();

    public abstract Class getJavaType();


    public boolean beVoid() {
        return false;
    }

    public boolean isGenericType() {
        return false;
    }

    /***
     * 如果是泛型，返回泛型里的具体类
     * @return
     */
    public abstract String getSpecificType();


    public static JavaType ofClassName(String className) {
        if (StringUtils.isBlank(className) || VOID.equalsIgnoreCase(className))
            return VOID_TYPE;
        return new JavaTypeByName(className);
    }

    public static JavaType ofClass(Class clz) {
        if (clz == null)
            return VOID_TYPE;
        return new JavaTypeByClass(clz);
    }

    public static JavaType ofType(Type type) {
        if (type == null)
            return VOID_TYPE;
        if (type instanceof Class)
            return ofClass((Class) type);
        return new JavaTypeByType(type);
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

    public abstract String getFullName();


    public static class VoidClass extends JavaType {

        @Override
        public boolean beVoid() {
            return true;
        }

        @Override
        public String getSpecificType() {
            return null;
        }

        @Override
        public String getFullName() {
            return getName();
        }

        @Override
        public String getName() {
            return VOID;
        }

        @Override
        public Class getJavaType() {
            return null;
        }
    }

    public static class JavaTypeByName extends JavaType {
        private String name;
        private String pack;

        public JavaTypeByName(String name) {
            if (ReflectionUtils.isFullClassName(name)) {
                this.name = ReflectionUtils.getSimpleClassName(name);
                pack = ReflectionUtils.getPackage(name);
            } else
                this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        public Class getJavaType() {
            return ReflectionUtils.tryGuessType(name);
        }

        public boolean isGenericType() {
            return name.contains("<");
        }

        public String getSpecificType() {
            if (!isGenericType())
                return null;
            return ReflectionUtils.findFirst(name, "<([^\\[\\]]+)>", matcher -> matcher.group(1));
        }

        @Override
        public String getFullName() {
            if (StringUtils.isBlank(pack))
                return name;
            return pack + "." + name;
        }
    }

    public static class JavaTypeByClass extends JavaType {
        private Class clz;

        public JavaTypeByClass(Class clz) {
            this.clz = clz;
        }

        @Override
        public String getName() {
            return clz.getSimpleName();
        }

        public Class getJavaType() {
            return clz;
        }

        public boolean isGenericType() {
            return false;
        }

        public String getSpecificType() {
            return getName();
        }

        @Override
        public String getFullName() {
            return clz.getName();
        }
    }

    public static class JavaTypeByType extends JavaType {
        private Type type;

        public JavaTypeByType(Type clz) {
            this.type = clz;
        }

        @Override
        public String getName() {
            return type.getTypeName();
        }

        public Class getJavaType() {
            return null;
        }

        public boolean isGenericType() {
            return type instanceof ParameterizedType;
        }

        public String getSpecificType() {
            if (!isGenericType())
                return getName();
            return ((ParameterizedType) type).getActualTypeArguments()[0].getTypeName();
        }

        @Override
        public String getFullName() {
            return type.getTypeName();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JavaType javaType = (JavaType) o;

        return getFullName() != null ? getFullName().equals(javaType.getFullName()) : javaType.getFullName() == null;
    }

    @Override
    public int hashCode() {
        return getFullName() != null ? getFullName().hashCode() : 0;
    }

    @Override
    public String toString() {
        return getName();
    }
}
