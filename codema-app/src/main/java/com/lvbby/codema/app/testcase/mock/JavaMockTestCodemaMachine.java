package com.lvbby.codema.app.testcase.mock;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaField;
import com.lvbby.codema.java.entity.JavaMethod;
import com.lvbby.codema.java.inject.JavaTemplate;
import com.lvbby.codema.java.inject.JavaTemplateInjector;
import com.lvbby.codema.java.inject.JavaTemplateParameter;
import com.lvbby.codema.java.result.JavaTemplateResult;
import com.lvbby.codema.java.tool.JavaCodeUtils;
import com.lvbby.codema.java.tool.JavaLexer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by dushang.lp on 2017/5/31.
 */
public class JavaMockTestCodemaMachine implements CodemaInjectable {

    @ConfigBind(JavaMockTestCodemaConfig.class)
    @CodemaRunner
    @JavaTemplate
    public void code(CodemaContext codemaContext, @NotNull JavaMockTestCodemaConfig config, @NotNull @JavaTemplateParameter(identifier = JavaTemplateInjector.java_source) JavaClass cu) throws Exception {
        config.handle(codemaContext, config,
                new JavaTemplateResult(config, $Mock_Test.class, cu)
                        .bind("Mock", cu.getName() + "Test")
                        .bind("injectFields", extractAllInjectFields(cu, config.getDependencyAnnotation()))
                        .registerResult()
        );
    }

    public static List<JavaField> extractAllInjectFields(JavaClass javaClass, List<String> annotations) {
        return extractAllInjectFields(javaClass, javaField -> isInjectField(javaField, annotations));
    }

    public static List<JavaField> extractAllInjectFields(JavaClass javaClass, Predicate<JavaField> predicate) {
        if (CollectionUtils.isNotEmpty(javaClass.getFields())) {
            return javaClass.getFields().stream().filter(javaField -> predicate == null || predicate.test(javaField)).collect(Collectors.toList());
        }
        return null;
    }

    private static boolean isInjectField(JavaField field) {
        return isInjectField(field, Lists.newArrayList(Autowired.class, Resource.class));
    }

    private static boolean isInjectField(JavaField field, List annotations) {
        return !field.getType().beVoid()
                && !field.getType().bePrimitive()
                && JavaCodeUtils.isOuterClass(field.getType().getJavaType())
                && (CollectionUtils.isEmpty(annotations) || CollectionUtils.containsAny(field.getAnnotations(), annotations))
                ;
    }

    public List<MockMethod> parseMockMethods(JavaClass clz) {
        if (clz.getFrom() != null && clz.getFrom() instanceof CompilationUnit && CollectionUtils.isNotEmpty(clz.getMethods())) {
            return JavaLexer.getClass((CompilationUnit) clz.getFrom())
                    .map(classOrInterfaceDeclaration ->
                            clz.getMethods().stream()
                                    .map(javaMethod -> MockMethod.from(clz, javaMethod, classOrInterfaceDeclaration))
                                    .collect(Collectors.toList()))
                    .orElse(null);
        }
        return Lists.newLinkedList();
    }

    private static class MockMethod {
        private JavaMethod javaMethod;
        private Set<MockDependencyMethod> dependencyMethods;

        public static MockMethod from(JavaClass javaClass, JavaMethod method) {
            MockMethod mockMethod = new MockMethod();
            mockMethod.setJavaMethod(method);

            MethodDeclaration methodDeclaration = findMethod(classOrInterfaceDeclaration, method);
            String ms = methodDeclaration.toString();
            List<JavaField> javaFields = JavaMockTestCodemaMachine.extractAllInjectFields(javaClass, null);
            if (CollectionUtils.isNotEmpty(javaFields)) {
                mockMethod.setDependencyMethods(Sets.newHashSet());//init
                javaFields.forEach(javaField -> mockMethod.getDependencyMethods().addAll(findReferredInvoke(ms, javaField)));
            }
            return mockMethod;
        }

        private static List<MockDependencyMethod> findReferredInvoke(String s, JavaField javaField) {
            return ReflectionUtils.findAllConvert(s,
                    StringUtils.uncapitalize(javaField.getName()) + "\\.([^\\(\\)]+)\\(",
                    matcher -> new MockDependencyMethod(javaField, matcher.group(1)));
        }

        private static MethodDeclaration findMethod(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, JavaMethod method) {
            List<MethodDeclaration> methodsByName = classOrInterfaceDeclaration.getMethodsByName(method.getName());
            if (CollectionUtils.isNotEmpty(methodsByName)) {
                if (methodsByName.size() == 1)
                    return methodsByName.get(0);
                //TODO
            }
            return null;
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

    private static class MockDependencyMethod {
        private JavaField javaField;
        private JavaMethod method;

        public MockDependencyMethod(JavaField javaField, JavaMethod method) {
            this.javaField = javaField;
            this.method = method;
        }

        public JavaField getJavaField() {
            return javaField;
        }

        public void setJavaField(JavaField javaField) {
            this.javaField = javaField;
        }

        public JavaMethod getMethod() {
            return method;
        }

        public void setMethod(JavaMethod method) {
            this.method = method;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MockDependencyMethod that = (MockDependencyMethod) o;

            if (!javaField.equals(that.javaField)) return false;
            return method.equals(that.method);
        }

        @Override
        public int hashCode() {
            int result = javaField.hashCode();
            result = 31 * result + method.hashCode();
            return result;
        }
    }
}