package com.lvbby.codema.app.springboot;

import com.lvbby.codema.java.baisc.TemplateResource;
import com.lvbby.codema.java.machine.AbstractJavaBaseMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;

/**
 * Created by lipeng on 16/12/23.
 */
@TemplateResource(MainApp.class)
public class JavaSpringBootMachine extends AbstractJavaBaseMachine {

    @Override
    protected void doCode() throws Exception {
        handle(new JavaTemplateResult(this, getTemplate(), null));
    }
}
