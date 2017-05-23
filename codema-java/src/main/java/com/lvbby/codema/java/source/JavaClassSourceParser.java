package com.lvbby.codema.java.source;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.SourceParser;
import com.lvbby.codema.core.error.CodemaException;
import com.lvbby.codema.core.utils.CodemaUtils;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.baisc.JavaSourceParam;
import com.lvbby.codema.java.entity.*;
import org.apache.commons.collections.CollectionUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 17/1/9.
 */
public class JavaClassSourceParser implements SourceParser<JavaSourceParam> {
    public static final String schema = "java://class/";

    @Override
    public String getSupportedUriScheme() {
        return schema;
    }

    public static String toURI(Class clz) {
        return schema + clz.getName();
    }

    @Override
    public JavaSourceParam parse(URI from) throws Exception {
        List<JavaClass> re = Lists.newLinkedList();
        Collection<Class<?>> allClasses = ReflectionUtils.loadClasses(CodemaUtils.getPathPart(from));
        if (CollectionUtils.isEmpty(allClasses))
            throw new CodemaException("no class found in " + from);
        for (Class<?> clz : allClasses)
            re.add(toJavaClass(clz));
        return new JavaSourceParam(re);
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
            javaField.setType(JavaType.ofField(ReflectionUtils.getField(clz, p.getName())));
            javaField.setPrimitive(p.getPropertyType().isPrimitive());
            return javaField;
        }).collect(Collectors.toList()));
        re.setMethods(Lists.newArrayList(beanInfo.getMethodDescriptors()).stream()
                .filter(methodDescriptor -> Modifier.isPublic(methodDescriptor.getMethod().getModifiers()))
                .map(m -> {
                    JavaMethod javaMethod = new JavaMethod();
                    javaMethod.setName(m.getName());
                    javaMethod.setReturnType(JavaType.ofMethodReturnType(m.getMethod()));
                    javaMethod.setArgs(Lists.newArrayList(m.getMethod().getParameters()).stream().map(parameterDescriptor -> {
                        JavaArg javaArg = new JavaArg();
                        javaArg.setName(parameterDescriptor.getName());
                        javaArg.setType(JavaType.ofMethodParameter(parameterDescriptor));
                        return javaArg;
                    }).collect(Collectors.toList()));
                    return javaMethod;
                }).collect(Collectors.toList()));
        return re;
    }

}
