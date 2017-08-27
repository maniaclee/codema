package com.lvbby.codema.app.bean;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaCodemaMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaBeanCodemaMachine extends AbstractJavaCodemaMachine<JavaBeanCodemaConfig> {

    public void codeEach(CodemaContext codemaContext, JavaBeanCodemaConfig c, JavaClass javaClass)
            throws Exception {
        JavaTemplateResult re = new JavaTemplateResult(c, $ClassName_.class, javaClass)
                .bind("ClassName", c.getJavaClassNameParser().getClassName(javaClass));
        c.handle(codemaContext, re);
    }

}
