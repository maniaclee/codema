package com.lvbby.codema.java.app.bean;

import com.google.common.collect.Maps;
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

import java.util.Map;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaBeanCodemaMachine implements CodemaInjectable {

    @ConfigBind(JavaBeanCodemaConfig.class)
    @CodemaRunner
    @JavaTemplate
    public void code(CodemaContext codemaContext, @NotNull JavaBeanCodemaConfig config, @NotNull @JavaTemplateParameter(identifier = JavaTemplateInjector.java_source) JavaClass javaClass) throws Exception {
        for (JavaBeanCodemaConfig javaBeanCodemaConfig : CodemaUtils.getAllConfig(config, JavaBeanCodemaConfig::getList))
            config.handle(codemaContext, javaBeanCodemaConfig, new JavaTemplateResult(javaBeanCodemaConfig, $ClassName_.class, javaClass, getArgs(javaBeanCodemaConfig, javaClass)));
    }

    private String getBeanName(JavaBeanCodemaConfig codemaConfig, JavaClass javaClass) throws Exception {
        return ObjectUtils.firstNonNull(ScriptEngineFactory.instance.eval(codemaConfig.getDestClassName(), javaClass.getName()), javaClass.getName());
    }

    private Map getArgs(JavaBeanCodemaConfig codemaConfig, JavaClass javaClass) throws Exception {
        Map map = Maps.newHashMap();
        map.put("ClassName", getBeanName(codemaConfig, javaClass));
        return map;
    }


}
