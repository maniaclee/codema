package com.lvbby.codema.app.convert;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.CodemaMachine;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.result.JavaTemplateResult;
import com.lvbby.codema.java.tool.JavaCodemaUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaConvertCodemaMachine implements CodemaMachine<JavaConvertCodemaConfig> {

    public void code(CodemaContext codemaContext, JavaConvertCodemaConfig config) throws Exception {
        Validate.notBlank(config.getConvertToClassName(), "convert-to-class-name can't be blank");
        if (StringUtils.isBlank(config.getDestClassName()))
            config.setDestClassName("BuildUtils");

        List<JavaClass> javaClasses = JavaCodemaUtils.findBeansByPackage(codemaContext, config);
        if (CollectionUtils.isEmpty(javaClasses))
            return;


        config.handle(codemaContext, new JavaTemplateResult(config, $Convert_.class)
                .bind("Convert", config.getDestClassName())
                .bind("cs", javaClasses)
                .bind("map", javaClasses.stream().collect(Collectors.toMap(o -> o, javaClass -> ReflectionUtils.getSimpleClassName(getTargetClassName(config, javaClass)))))
                .registerResult());
    }

    private String getTargetClassName(JavaConvertCodemaConfig config, JavaClass javaClass) {
        return config.eval(config.getConvertToClassName(), javaClass.getName());
    }


}
