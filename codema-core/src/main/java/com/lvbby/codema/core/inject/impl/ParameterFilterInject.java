package com.lvbby.codema.core.inject.impl;

import com.lvbby.codema.core.inject.CodemaInjectContext;
import com.lvbby.codema.core.inject.CodemaInjector;
import com.lvbby.codema.core.inject.InjectInterruptException;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.core.utils.OrderValue;

/**
 * Created by lipeng on 16/12/27.
 */

@OrderValue(Integer.MAX_VALUE - 1000)
public class ParameterFilterInject implements CodemaInjector {
    @Override
    public void process(CodemaInjectContext context) {
        if (context.getArgs().stream().anyMatch(injectEntry -> (injectEntry.getValue() == null && injectEntry.getParameter().isAnnotationPresent(NotNull.class))))
            throw new InjectInterruptException("required not null parameters");
    }

}
