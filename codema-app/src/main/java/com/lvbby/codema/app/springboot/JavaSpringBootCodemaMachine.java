package com.lvbby.codema.app.springboot;

import com.lvbby.codema.core.AbstractCodemaMachine;
import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.result.JavaTemplateResult;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaSpringBootCodemaMachine extends AbstractCodemaMachine<JavaSpringBootConfig,Object,JavaClass> {

    public Result<JavaClass> codeEach(JavaSpringBootConfig config, Object source) throws Exception {
        return new JavaTemplateResult(config, MainApp.class);
    }

}
