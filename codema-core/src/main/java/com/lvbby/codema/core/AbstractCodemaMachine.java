package com.lvbby.codema.core;

import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.utils.ReflectionUtils;

import java.io.IOException;

/**
 *
 * @author dushang.lp
 * @version $Id: AbstractCodemaMachine.java, v 0.1 2017-08-24 обнГ3:32 dushang.lp Exp $
 */
public abstract class AbstractCodemaMachine<T extends CommonCodemaConfig>
                                           implements CodemaMachine<T> {

    protected String loadResourceAsString(String resourceName) throws IOException {
        return ReflectionUtils.loadResource(getClass(), resourceName);
    }
}