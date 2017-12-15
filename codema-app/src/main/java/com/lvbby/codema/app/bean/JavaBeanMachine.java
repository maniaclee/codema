package com.lvbby.codema.app.bean;

import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.java.baisc.TemplateResource;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.JavaMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;

/**@apiNote JavaBean
 * @author  lipeng on 2017/1/7.
 */
@TemplateResource($ClassName_.class)
public class JavaBeanMachine extends JavaMachine {
    @Override public Result<JavaClass> codeEach(JavaClass cu) throws Exception {
        return new JavaTemplateResult(this, getTemplate(), cu);
    }
}
