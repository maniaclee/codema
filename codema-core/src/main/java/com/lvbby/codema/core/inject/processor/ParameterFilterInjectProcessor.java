package com.lvbby.codema.core.inject.processor;

import com.lvbby.codema.core.inject.CodemaInjectContext;
import com.lvbby.codema.core.inject.CodemaInjectorProcessor;
import com.lvbby.codema.core.inject.InjectInterruptException;
import com.lvbby.codema.core.inject.NotNull;

/**
 * Created by lipeng on 16/12/27.
 */
public class ParameterFilterInjectProcessor implements CodemaInjectorProcessor {
    @Override
    public void process(CodemaInjectContext context) {
        if (context.getArgs().stream().anyMatch(injectEntry -> !(injectEntry.getValue() == null && injectEntry.getParameter().isAnnotationPresent(NotNull.class))))
            throw new InjectInterruptException("required not null parameters");
    }
}
