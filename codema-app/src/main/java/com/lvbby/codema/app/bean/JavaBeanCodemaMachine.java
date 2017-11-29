package com.lvbby.codema.app.bean;

import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaInputCodemaMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;

/**@apiNote JavaBean
 * @author  lipeng on 2017/1/7.
 */
public class JavaBeanCodemaMachine extends AbstractJavaInputCodemaMachine {
    @Override public Result<JavaClass> codeEach(JavaClass cu) throws Exception {
        return new JavaTemplateResult(this, $ClassName_.class, cu);
    }
}
