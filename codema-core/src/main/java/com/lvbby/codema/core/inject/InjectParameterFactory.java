package com.lvbby.codema.core.inject;

import com.lvbby.codema.core.bean.CodemaBean;

/**
 * Created by lipeng on 2017/1/1.
 */
public interface InjectParameterFactory {

    CodemaBean create(CodemaInjectContext codemaInjectContext, String id);
}
