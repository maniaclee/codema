package com.lvbby.codema.app.springboot;

import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.java.baisc.TemplateResource;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.JavaMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;

/**
 * Created by lipeng on 16/12/23.
 */
@TemplateResource(MainApp.class)
public class JavaSpringBootMachine extends JavaMachine {

    public Result<JavaClass> codeEach(JavaClass source) throws Exception {
        return new JavaTemplateResult(this, getTemplate(),null);
    }

}
