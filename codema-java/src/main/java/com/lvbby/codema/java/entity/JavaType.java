package com.lvbby.codema.java.entity;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.utils.ReflectionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by lipeng on 2017/1/10.
 */
public class JavaType {
    private static final String VOID = "void";
    private static final JavaType VOID_TYPE = new JavaType(VOID);
    /**
     * 具体类型,rawType
     */
    private Class type;

    /**
     * 泛型
     */
    private List<JavaType> genericTypes;
    /**
     * 简单的描述 如果是泛型为List<String>
     */
    private String name;
    private String pack;

    private JavaType(String name) {
        this.name = name;
    }

    public JavaType() {
    }

    public boolean bePrimitive() {
        if (type != null)
            return type.isPrimitive();
        if (beVoid())
            return false;
        String name = getName();
        return StringUtils.isNotBlank(name) && StringUtils.isAllLowerCase(name);
    }

    public String getName() {
        return name;
    }

    public Class getJavaType() {
        return type;
    }


    public boolean beVoid() {
        return VOID.equals(getName());
    }

    public boolean isGenericType() {
        return CollectionUtils.isNotEmpty(genericTypes);
    }

    /***
     * 如果是泛型，返回泛型里的具体类
     * @return
     */
    public String getSpecificType() {
        return getName();//TODO
    }


    public static void main(String[] args) {
        //        GenericClass re = GenericClass.of("Map<Map<String,String>,String>");
        //        System.out.println(re);
        //        System.out.println(GenericClass.of("Map<String>"));
        //        System.out.println(GenericClass.of("String"));
        //        System.out.println(splitGeneric("Map<String,Map<Integer,Class>>,String"));
        System.out.println(ofClassName("Map<String,Map<Integer,Class>,String>"));
        System.out.println(ofClassName("boolean").getFullName());
    }

    private static List<String> splitGeneric(String s) {
        return split(s, ',', '<', '>');
    }

    private static List<String> split(String s, char spliter, char left, char right) {
        List<String> re = Lists.newArrayList();
        int open = 0;
        int index = 0;
        for (int i = 0; i < s.length(); i++) {
            char cur = s.charAt(i);
            if (cur == spliter) {
                if (open > 0) {
                    continue;
                } else {
                    re.add(s.substring(index, i));
                    index = ++i;
                }
            }
            if (cur == left) {
                open++;
            }
            if (cur == right)
                open--;
        }
        if (open == 0) {
            re.add(s.substring(index));
        }
        if (open != 0)
            throw new IllegalArgumentException("invalid class name:" + s);
        return re;
    }

    public static JavaType ofClassName(String className) {
        if (StringUtils.isBlank(className) || VOID.equalsIgnoreCase(className))
            return VOID_TYPE;
        JavaType javaType = new JavaType();
        javaType.pack = ReflectionUtils.getPackage(className);
        String name = ReflectionUtils.getSimpleClassName(className);
        javaType.name = name;
        if (!name.contains("<"))
            return javaType;
        int start = name.indexOf('<');
        int end = name.lastIndexOf('>');
        if (start > 0 && end > start) {
            List<String> genericTypes = splitGeneric(name.substring(start + 1, end));
            javaType.genericTypes = genericTypes.stream().map(t -> ofClassName(t)).collect(Collectors.toList());
            return javaType;
        }
        throw new IllegalArgumentException("invalid class name");
    }

    public static JavaType ofClass(Class clz) {
        if (clz == null)
            return VOID_TYPE;
        JavaType re = new JavaType();
        re.name = clz.getSimpleName();
        re.type = clz;
        re.pack = Optional.ofNullable(clz.getPackage()).map(Package::getName).orElse("");
        return re;
    }

    public static JavaType ofType(Type type) {
        if (type == null)
            return VOID_TYPE;
        if (type instanceof Class)
            return ofClass((Class) type);
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            JavaType re = ofClass((Class) parameterizedType.getRawType());
            re.genericTypes = Stream.of(parameterizedType.getActualTypeArguments()).map(t -> ofType(t)).collect(Collectors.toList());
            return re;
        }
        //        if (type instanceof TypeVariable) {
        //            GenericDeclaration genericDeclaration = ((TypeVariable) type).getGenericDeclaration();
        //            if (genericDeclaration instanceof Method)
        //        }
        throw new IllegalArgumentException("unknown type:" + type.toString());
    }

    public static JavaType ofMethodReturnType(Method method) {
        //        return ofType(ObjectUtils.firstNonNull(method.getGenericReturnType(), method.getReturnType()));
        return ofType(ObjectUtils.firstNonNull(method.getReturnType()));
    }

    public static JavaType ofField(Field field) {
        return ofType(ObjectUtils.firstNonNull(field.getGenericType(), field.getType()));
    }

    public static JavaType ofMethodParameter(Parameter parameter) {
        //        return ofType(ObjectUtils.firstNonNull(parameter.getParameterizedType(), parameter.getType()));
        return ofType(ObjectUtils.firstNonNull(parameter.getType()));
    }

    public String getFullName() {
        if (StringUtils.isBlank(pack))
            return name;
        return pack + "." + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null)
            return false;
        if (!(o instanceof JavaType)) {
            if (o instanceof Class) {
                return Objects.equals(((Class) o).getName(), getFullName());
            }
        }
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
