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
import org.apache.commons.collections.CollectionUtils;
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

    private List<JavaField> extractAllInjectFields(JavaClass javaClass) {
        if (CollectionUtils.isNotEmpty(javaClass.getFields())) {
            return javaClass.getFields().stream().filter(javaField -> isInjectField(javaField)).collect(Collectors.toList());
        }
        return null;
    }

    private boolean isInjectField(JavaField field) {
        return !field.getType().beVoid()
                && !field.getType().bePrimitive()
                && CollectionUtils.containsAny(field.getAnnotations(), Lists.newArrayList(Autowired.class, Resource.class))
                ;
    }
}