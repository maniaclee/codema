package com.lvbby.codema.app.testcase.mock;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaField;
import com.lvbby.codema.java.entity.JavaMethod;
import com.lvbby.codema.java.tool.JavaCodeUtils;
import com.lvbby.codema.java.tool.JavaLexer;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2017/6/9.
 */
public class MockMethod {
    private JavaMethod javaMethod;
    private Set<MockDependencyMethod> dependencyMethods;


    public static List<MockMethod> parseMockMethods(JavaClass clz, Predicate<JavaField> predicate) {
        if (clz.getSrc() != null && CollectionUtils.isNotEmpty(clz.getMethods())) {
            List<JavaField> fields = clz.getFields().stream().filter(javaField -> predicate == null || predicate.test(javaField)).collect(Collectors.toList());
            return JavaLexer.getClass(clz.getSrc())
                    .map(classOrInterfaceDeclaration ->
                            clz.getMethods().stream()
                                    .map(javaMethod -> MockMethod.from(clz, javaMethod, fields))
                                    .collect(Collectors.toList()))
                    .orElse(null);
        }
        return Lists.newLinkedList();
    }

    public static MockMethod from(JavaClass javaClass, JavaMethod method, List<JavaField> javaFields) {
        MockMethod mockMethod = new MockMethod();
        mockMethod.setJavaMethod(method);

        String ms = method.getSrc().toString();
        if (CollectionUtils.isNotEmpty(javaFields)) {
            mockMethod.setDependencyMethods(Sets.newHashSet());//init
            javaFields.forEach(javaField -> mockMethod.getDependencyMethods().addAll(findReferredInvoke(ms, javaField, javaClass)));
        }
        return mockMethod;
    }

    private static List<MockDependencyMethod> findReferredInvoke(String s, JavaField javaField, JavaClass javaClass) {
        return ReflectionUtils.findAllConvert(s,
                javaField.getName() + "\\.([^\\(\\)]+)\\(",
                matcher -> {
                    JavaMethod method = JavaCodeUtils.findMethod(javaField.tryFindJavaClass(), matcher.group(1));
                    return method == null ? null : new MockDependencyMethod(javaField, method);
                });
    }

    public JavaMethod getJavaMethod() {
        return javaMethod;
    }

    public void setJavaMethod(JavaMethod javaMethod) {
        this.javaMethod = javaMethod;
    }

    public Set<MockDependencyMethod> getDependencyMethods() {
        return dependencyMethods;
    }

    public void setDependencyMethods(Set<MockDependencyMethod> dependencyMethods) {
        this.dependencyMethods = dependencyMethods;
    }

}
