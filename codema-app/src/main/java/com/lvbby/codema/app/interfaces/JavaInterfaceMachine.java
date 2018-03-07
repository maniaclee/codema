package com.lvbby.codema.app.interfaces;

import org.apache.commons.lang3.Validate;

import com.lvbby.codema.app.AppMachine;
import com.lvbby.codema.app.AppTemplateResource;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.result.JavaTemplateResult;

/**
 * Created by lipeng on 17/1/6.
 */
@AppTemplateResource($Interface_.class)
public class JavaInterfaceMachine extends AppMachine {

    @Override
    public JavaTemplateResult codeEach(JavaClass cu) throws Exception {
        Validate.notNull(getDestPackage(),"dest package can't be null");
        return super.codeEach(cu);
    }
}
