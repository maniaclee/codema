package com.lvbby.codema.java.source;

import com.github.javaparser.ast.CompilationUnit;
import com.google.common.collect.Lists;
import com.lvbby.codema.core.SourceParser;
import com.lvbby.codema.core.error.CodemaException;
import com.lvbby.codema.core.utils.CodemaUtils;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.baisc.JavaSourceParam;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaField;
import com.lvbby.codema.java.entity.JavaMethod;
import com.lvbby.codema.java.tool.JavaCodeUtils;
import com.lvbby.codema.java.tool.JavaLexer;
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
        CompilationUnit src = JavaCodeUtils.loadJavaSrcFromProject(clz);

        JavaClass re = new JavaClass();
        re.setName(clz.getSimpleName());
        re.setFrom(clz);
        re.setPack(clz.getPackage().getName());
        re.setType(clz);

        BeanInfo beanInfo = Introspector.getBeanInfo(clz, Object.class);
        re.setFields(Lists.newArrayList(beanInfo.getPropertyDescriptors()).stream()
                .map(p -> JavaField.from(ReflectionUtils.getField(clz, p.getName())))
                .collect(Collectors.toList()));
        re.setMethods(Lists.newArrayList(beanInfo.getMethodDescriptors()).stream()
                .filter(methodDescriptor -> Modifier.isPublic(methodDescriptor.getMethod().getModifiers()))
                .map(m -> JavaMethod.from(m.getMethod()).src(JavaLexer.getClass(src).orElse(null)))
                .collect(Collectors.toList()));
        re.setSrc(src);
        return re;
    }

}
