package com.lvbby.codema.java.source;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.SourceParser;
import com.lvbby.codema.core.utils.CodemaUtils;
import com.lvbby.codema.java.app.baisc.JavaSourceParam;
import com.lvbby.codema.java.entity.JavaArg;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaField;
import com.lvbby.codema.java.entity.JavaMethod;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 17/1/9.
 */
public class JavaClassSourceParser implements SourceParser<JavaSourceParam> {
    @Override
    public String getSupportedUriScheme() {
        return "java://class/";
    }

    @Override
    public JavaSourceParam parse(URI from) throws Exception {
        Class<?> clz = Class.forName(CodemaUtils.getPathPart(from));
        return new JavaSourceParam(Lists.newArrayList(toJavaClass(clz)));
    }

    public static JavaClass toJavaClass(Class clz) throws Exception {
        JavaClass re = new JavaClass();
        re.setName(clz.getSimpleName());
        re.setFrom(clz);
        re.setPack(clz.getPackage().getName());
        BeanInfo beanInfo = Introspector.getBeanInfo(clz, Object.class);
        re.setFields(Lists.newArrayList(beanInfo.getPropertyDescriptors()).stream().map(p -> {
            JavaField javaField = new JavaField();
            javaField.setName(p.getName());
            javaField.setType(p.getShortDescription());//TODO like List<ResultHandler>
            javaField.setPrimitive(p.getPropertyType().isPrimitive());
            return javaField;
        }).collect(Collectors.toList()));
        re.setMethods(Lists.newArrayList(beanInfo.getMethodDescriptors()).stream()
                .filter(methodDescriptor -> Modifier.isPublic(methodDescriptor.getMethod().getModifiers()))
                .map(m -> {
                    JavaMethod javaMethod = new JavaMethod();
                    javaMethod.setName(m.getName());
                    javaMethod.setReturnType(getClassType(m.getMethod().getReturnType(), m.getMethod().getGenericReturnType()));
                    javaMethod.setArgs(Lists.newArrayList(m.getMethod().getParameters()).stream().map(parameterDescriptor -> {
                        JavaArg javaArg = new JavaArg();
                        javaArg.setName(parameterDescriptor.getName());
                        javaArg.setType(getClassType(parameterDescriptor.getType()));
                        return javaArg;
                    }).collect(Collectors.toList()));
                    return javaMethod;
                }).collect(Collectors.toList()));
        return re;
    }

    private static String getClassType(Type... clz) {
        String re = null;
        if (clz != null && clz.length > 0) {
            for (Type type : clz) {
                if (type instanceof ParameterizedType) {
                    return getGenericType((ParameterizedType) type);
                }
                if (type instanceof Class)
                    re = ((Class) type).getSimpleName();
            }
        }
        return re;
    }

    private static String getGenericType(ParameterizedType type) {
        String simpleName = ((Class) type.getRawType()).getSimpleName();
        String types = Lists.newArrayList(type.getActualTypeArguments()).stream().map(t -> (Class) t).map(Class::getSimpleName).collect(Collectors.joining(","));
        return String.format("%s<%s>", simpleName, types);
    }
}
