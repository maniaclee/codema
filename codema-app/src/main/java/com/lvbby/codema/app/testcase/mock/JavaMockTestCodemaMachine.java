package com.lvbby.codema.app.testcase.mock;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.config.ConfigProperty;
import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaField;
import com.lvbby.codema.java.machine.AbstractJavaCodemaMachine;
import com.lvbby.codema.java.machine.AbstractJavaInputCodemaMachine;
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
public class JavaMockTestCodemaMachine extends AbstractJavaInputCodemaMachine {

    @ConfigProperty
    private List annotations ;
    public Result<JavaClass> codeEach(  JavaClass cu)
            throws Exception {
        return new JavaTemplateResult(this, $Mock_Test.class, cu)
                .bind("Mock", cu.getName() + "Test")
                .bind("injectFields", extractAllInjectFields(cu,  getAnnotations()))
                .bind("methods", MockMethod
                        .parseMockMethods(cu, javaField -> javaField.getType().beOutterClass()));
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

    /**
     * Getter method for property   annotations.
     *
     * @return property value of annotations
     */
    public List getAnnotations() {
        return annotations;
    }

    /**
     * Setter method for property   annotations .
     *
     * @param annotations  value to be assigned to property annotations
     */
    public void setAnnotations(List annotations) {
        this.annotations = annotations;
    }
}