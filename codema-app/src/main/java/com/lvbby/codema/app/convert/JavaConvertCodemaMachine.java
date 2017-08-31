package com.lvbby.codema.app.convert;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaCodemaMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaConvertCodemaMachine extends AbstractJavaCodemaMachine<JavaConvertCodemaConfig> {

    @Override
    protected void codeEach(CodemaContext codemaContext, JavaConvertCodemaConfig config,
                            JavaClass javaClass) throws Exception {
        if (StringUtils.isBlank(config.getDestClassName()))
            config.setDestClassName("BuildUtils");

        config.handle(codemaContext, new JavaTemplateResult(config, $Convert_.class, javaClass));
    }
}
