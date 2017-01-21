package com.lvbby.codema.app.bean;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.engine.ScriptEngineFactory;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.core.utils.CodemaUtils;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.inject.JavaTemplate;
import com.lvbby.codema.java.inject.JavaTemplateInjector;
import com.lvbby.codema.java.inject.JavaTemplateParameter;
import com.lvbby.codema.java.result.JavaTemplateResult;
import org.apache.commons.lang3.ObjectUtils;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaBeanCodemaMachine implements CodemaInjectable {

    @ConfigBind(JavaBeanCodemaConfig.class)
    @CodemaRunner
    @JavaTemplate
    public void code(CodemaContext codemaContext, @NotNull JavaBeanCodemaConfig config, @NotNull @JavaTemplateParameter(identifier = JavaTemplateInjector.java_source) JavaClass javaClass) throws Exception {
        for (JavaBeanCodemaConfig c : CodemaUtils.getAllConfig(config, JavaBeanCodemaConfig::getList)) {
            JavaTemplateResult re = new JavaTemplateResult(c, "templates/$ClassName_.java", javaClass)
                    .bind("ClassName", ObjectUtils.firstNonNull(ScriptEngineFactory.instance.eval(c.getDestClassName(), javaClass.getName()), javaClass.getName()))
                    .registerResult();//注册
            config.handle(codemaContext, c, re);
        }
    }
}
