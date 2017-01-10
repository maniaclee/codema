package com.lvbby.codema.java.app.repository;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.app.dto.$src__name_DTO;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaMethod;
import com.lvbby.codema.java.inject.JavaTemplate;
import com.lvbby.codema.java.inject.JavaTemplateInjector;
import com.lvbby.codema.java.inject.JavaTemplateParameter;
import com.lvbby.codema.java.result.JavaTemplateResult;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaRepositoryCodemaMachine implements CodemaInjectable {

    @ConfigBind(JavaRepositoryCodemaConfig.class)
    @CodemaRunner
    @JavaTemplate
    public void code(CodemaContext codemaContext, @NotNull JavaRepositoryCodemaConfig config, @NotNull @JavaTemplateParameter(identifier = JavaTemplateInjector.java_source) JavaClass javaClass) throws Exception {
        javaClass = ReflectionUtils.copy(javaClass, JavaClass.class);
        javaClass.getMethods().forEach(javaMethod -> {

        });
        config.handle(codemaContext, config, new JavaTemplateResult(config, $src__name_DTO.class, javaClass));
    }


    private static JavaClass findEntityFromDao(JavaMethod javaMethod) {
//        javaMethod.getArgs().stream().filter(javaArg -> javaArg.getType())
        return null;
    }
}
