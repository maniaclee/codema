package com.lvbby.codema.app.testcase.mock;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.google.common.collect.Lists;
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
import com.lvbby.codema.java.tool.JavaLexer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;
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
                        .bind("injectFields", extractAllInjectFields(cu))
                        .registerResult()
        );
    }

    public static List<JavaField> extractAllInjectFields(JavaClass javaClass) {
        if (CollectionUtils.isNotEmpty(javaClass.getFields())) {
            return javaClass.getFields().stream().filter(javaField -> isInjectField(javaField)).collect(Collectors.toList());
        }
        return null;
    }

    private static boolean isInjectField(JavaField field) {
        return !field.getType().beVoid()
                && !field.getType().bePrimitive()
                && CollectionUtils.containsAny(field.getAnnotations(), Lists.newArrayList(Autowired.class, Resource.class))
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
        private List<MockDependencyMethod> dependencyMethods;

        public static MockMethod from(JavaClass javaClass, JavaMethod method, ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
            MockMethod mockMethod = new MockMethod();
            mockMethod.setJavaMethod(method);
            MethodDeclaration methodDeclaration = findMethod(classOrInterfaceDeclaration, method);
            String ms = methodDeclaration.toString();
            List<JavaField> javaFields = JavaMockTestCodemaMachine.extractAllInjectFields(javaClass);
            if (CollectionUtils.isNotEmpty(javaFields)) {
                List<MockDependencyMethod> mockDependencyMethodList = Lists.newArrayList();
                javaFields.forEach(javaField -> mockDependencyMethodList.addAll(findReferredInvokation(ms, javaField)));
            }
            return mockMethod;
        }

        private static List<MockDependencyMethod> findReferredInvokation(String s, JavaField javaField) {
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

        public List<MockDependencyMethod> getDependencyMethods() {
            return dependencyMethods;
        }

        public void setDependencyMethods(List<MockDependencyMethod> dependencyMethods) {
            this.dependencyMethods = dependencyMethods;
        }
    }

    private static class MockDependencyMethod {
        private JavaField javaField;
        private String method;

        public MockDependencyMethod(JavaField javaField, String method) {
            this.javaField = javaField;
            this.method = method;
        }

        public JavaField getJavaField() {
            return javaField;
        }

        public void setJavaField(JavaField javaField) {
            this.javaField = javaField;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }
    }
}