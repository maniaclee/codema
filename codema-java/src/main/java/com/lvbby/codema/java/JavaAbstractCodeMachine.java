package com.lvbby.codema.java;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.TypedCodeMachine;
import com.lvbby.codema.java.baisc.CodemaJavaBasicConfig;

/**
 * Created by lipeng on 2016/12/26.
 */
public abstract class JavaAbstractCodeMachine<T> extends TypedCodeMachine<T> {
    @Override
    public void code(CodemaContext codemaContext, T config) throws Exception {
    }

    private void init(CodemaContext codemaContext, CodemaJavaBasicConfig config) {
    }

}
