package com.lvbby.codema.core;

/**
 * Created by lipeng on 16/12/23.
 */
public abstract class TypedCodeMachine<T> extends TypeCapable<T> implements CodemaMachine {
    @Override
    public void code(CodemaContext codemaContext) throws Exception {
        if (codemaContext.hasConfig(getType())) {
            code(codemaContext, codemaContext.getConfig(getType()));
        }
    }

    public abstract void code(CodemaContext codemaContext, T config) throws Exception;

}
