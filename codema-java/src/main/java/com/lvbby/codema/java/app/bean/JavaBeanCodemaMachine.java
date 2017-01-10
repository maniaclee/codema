package com.lvbby.codema.java.app.bean;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.core.utils.CodemaUtils;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.inject.JavaTemplate;
import com.lvbby.codema.java.inject.JavaTemplateInjector;
import com.lvbby.codema.java.inject.JavaTemplateParameter;
import com.lvbby.codema.java.result.JavaTemplateResult;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaBeanCodemaMachine implements CodemaInjectable {

    @ConfigBind(JavaBeanCodemaConfig.class)
    @CodemaRunner
    @JavaTemplate
    public void code(CodemaContext codemaContext, @NotNull JavaBeanCodemaConfig config, @NotNull @JavaTemplateParameter(identifier = JavaTemplateInjector.java_source) JavaClass javaClass) throws Exception {
        for (JavaBeanCodemaConfig javaBeanCodemaConfig : CodemaUtils.getAllConfig(config, JavaBeanCodemaConfig::getList))
            config.handle(codemaContext, javaBeanCodemaConfig, new JavaTemplateResult(javaBeanCodemaConfig, $src__name_.class, javaClass));
    }


}
