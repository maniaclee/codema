package com.lvbby.codema.app.bean;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.engine.ScriptEngineFactory;
import com.lvbby.codema.core.utils.CodemaUtils;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaCodemaMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;
import org.apache.commons.lang3.ObjectUtils;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaBeanCodemaMachine extends AbstractJavaCodemaMachine<JavaBeanCodemaConfig> {

    public void processSingle(CodemaContext codemaContext, JavaBeanCodemaConfig config, JavaClass javaClass) throws Exception {
        for (JavaBeanCodemaConfig c : CodemaUtils.getAllConfig(config, JavaBeanCodemaConfig::getList)) {
            JavaTemplateResult re = new JavaTemplateResult(c, $ClassName_.class, javaClass)
                    .bind("ClassName", ObjectUtils.firstNonNull(ScriptEngineFactory.instance.eval(c.getDestClassName(), javaClass.getName()), javaClass.getName()))
                    .registerResult();//注册
            config.handle(codemaContext, c, re);
        }
    }

}
