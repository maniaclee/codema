package com.lvbby.codema.app.convert;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.result.JavaTemplateResult;
import com.lvbby.codema.java.tool.JavaCodemaUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaConvertCodemaMachine implements CodemaInjectable {

    @ConfigBind(JavaConvertCodemaConfig.class)
    @CodemaRunner
    public void code(CodemaContext codemaContext, @NotNull JavaConvertCodemaConfig config) throws Exception {
        Validate.notBlank(config.getConvertToClassName(), "convert-to-class-name can't be blank");

        List<JavaClass> javaClasses = JavaCodemaUtils.findBeansByPackage(codemaContext, config);
        if (CollectionUtils.isEmpty(javaClasses))
            return;


        config.handle(codemaContext, config, new JavaTemplateResult(config, $Convert_.class)
                .bind("Convert", config.getDestClassName())
                .bind("cs", javaClasses)
                .bind("map", javaClasses.stream().collect(Collectors.toMap(o -> o, javaClass -> ReflectionUtils.getSimpleClassName(getTargetClassName(config, javaClass)))))
                .addImportJavaClasses(javaClasses)
                .addImportClassFullName(javaClasses.stream().map(javaClass -> getTargetClassName(config, javaClass)).collect(Collectors.toList()))
                .registerResult());
    }

    private String getTargetClassName(JavaConvertCodemaConfig config, JavaClass javaClass) {
        return config.eval(config.getConvertToClassName(), javaClass.getName());
    }


}
