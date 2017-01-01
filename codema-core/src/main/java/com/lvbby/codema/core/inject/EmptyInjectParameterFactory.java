package com.lvbby.codema.core.inject;

import com.lvbby.codema.core.resource.CodemaResource;

/**
 * Created by lipeng on 2017/1/1.
 */
public class EmptyInjectParameterFactory implements InjectParameterFactory {
    @Override
    public CodemaResource create(CodemaInjectContext codemaInjectContext, String id) {
        return null;
    }
}
