package com.lvbby.codema.java;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.machine.TypedCodeMachine;

/**
 * Created by lipeng on 2016/12/26.
 */
public abstract class AbstractJavaCodemaMachine<T, S> extends TypedCodeMachine<T> {
    @Override
    public void code(CodemaContext codemaContext, T config) throws Exception {
        if (getTypes().get(1).isAssignableFrom(codemaContext.getSource().getClass())) {
            code(codemaContext, config, (S) codemaContext.getSource());
        }
    }

    public abstract void code(CodemaContext codemaContext, T config, S source);

}
