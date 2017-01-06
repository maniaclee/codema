package com.lvbby.codema.core.inject;

import com.lvbby.codema.core.bean.CodemaBean;

/**
 * Created by lipeng on 2017/1/1.
 */
public class EmptyInjectParameterFactory implements InjectParameterFactory {
    @Override
    public CodemaBean create(CodemaInjectContext codemaInjectContext, String id) {
        return null;
    }
}
