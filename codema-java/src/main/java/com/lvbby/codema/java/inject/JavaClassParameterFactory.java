package com.lvbby.codema.java.inject;

import com.lvbby.codema.core.inject.CodemaInjectContext;
import com.lvbby.codema.core.inject.InjectParameterFactory;
import com.lvbby.codema.core.bean.CodemaBean;
import com.lvbby.codema.java.app.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.resource.JavaCodemaResoure;
import com.lvbby.codema.java.tool.JavaClassUtils;

/**
 * Created by lipeng on 2017/1/1.
 */
public class JavaClassParameterFactory implements InjectParameterFactory {
    @Override
    public CodemaBean create(CodemaInjectContext codemaInjectContext, String id) {
        JavaBasicCodemaConfig config = codemaInjectContext.findConfig(JavaBasicCodemaConfig.class);
        if (config == null)
            throw new IllegalArgumentException(String.format("%s not found in codemaInjectContext", config.getClass().getName()));
        return new JavaCodemaResoure(JavaClassUtils.createJavaClasssUnit(id, config.getAuthor(), config.isToBeInterface()));
    }
}
