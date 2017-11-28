package com.lvbby.codema.core;

import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.core.utils.TypeCapable;

/**
 *
 * @author dushang.lp
 * @version $Id: AbstractCodemaMachine.java, v 0.1 2017-08-24 3:32 dushang.lp Exp $
 */
public abstract class VoidCodemaMachine<T extends CommonCodemaConfig> extends TypeCapable<T>
                                           implements CodemaMachine<T,Object,Object> {

    @Override public Result<Object> code(T config, Object source) throws Exception {
        return null;
    }

    @Override public Class<T> getConfigType() {
        return getType();
    }

    @Override public Class getSourceType() {
        return null;
    }

    @Override public Class getDestType() {
        return null;
    }
}