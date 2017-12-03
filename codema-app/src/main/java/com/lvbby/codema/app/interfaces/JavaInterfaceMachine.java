package com.lvbby.codema.app.interfaces;

import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaInputMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;
import org.apache.commons.lang3.Validate;

/**
 * Created by lipeng on 17/1/6.
 */
public class JavaInterfaceMachine extends AbstractJavaInputMachine {
    @Override public Result<JavaClass> codeEach(JavaClass cu) throws Exception {
        Validate.notNull(getDestPackage(),"dest package can't be null");
        return new JavaTemplateResult(this, $Interface_.class, cu);
    }
}
