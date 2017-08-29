package com.lvbby.codema.java.machine;

import com.lvbby.codema.core.AbstractCodemaMachine;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.entity.JavaClass;

/**
 * Created by dushang.lp on 2017/8/16.
 */
public abstract class AbstractJavaCodemaMachine<T extends JavaBasicCodemaConfig> extends
        AbstractCodemaMachine<T> {
    @Override
    public void code(CodemaContext context, T config) throws Exception {
        preCode(context, config);
        //根据包名从source里面捞取
        if (context.getSource() instanceof JavaClass) {
            codeEach(context, config, (JavaClass) context.getSource());
        }
    }

    protected abstract void codeEach(CodemaContext context, T config, JavaClass javaClass)
            throws Exception;

    protected void preCode(CodemaContext context, T config) throws Exception {
    }
}
