package com.lvbby.codema.app.testcase.mock;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaField;
import com.lvbby.codema.java.inject.JavaTemplate;
import com.lvbby.codema.java.inject.JavaTemplateInjector;
import com.lvbby.codema.java.inject.JavaTemplateParameter;
import com.lvbby.codema.java.result.JavaTemplateResult;
import com.lvbby.codema.java.tool.JavaCodeUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;
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

    public static List<JavaField> extractAllInjectFields(JavaClass javaClass, List annotations) {
        return extractAllInjectFields(javaClass, javaField -> isInjectField(javaField, annotations));
    }

    public static List<JavaField> extractAllInjectFields(JavaClass javaClass) {
        return extractAllInjectFields(javaClass, Lists.newArrayList(Autowired.class, Resource.class));
    }

    public static List<JavaField> extractAllInjectFields(JavaClass javaClass, Predicate<JavaField> predicate) {
        if (CollectionUtils.isNotEmpty(javaClass.getFields())) {
            return javaClass.getFields().stream().filter(javaField -> predicate == null || predicate.test(javaField)).collect(Collectors.toList());
        }
        return null;
    }

    private static boolean isInjectField(JavaField field, List annotations) {
        return !field.getType().beVoid()
                && !field.getType().bePrimitive()
                && JavaCodeUtils.isOuterClass(field.getType().getJavaType())
                && (CollectionUtils.isEmpty(annotations) || CollectionUtils.containsAny(field.getAnnotations(), annotations))
                ;
    }


}