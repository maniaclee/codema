package com.lvbby.codema.core;

import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.utils.TypeCapable;

/**
 * 不关心input和output
 * @author dushang.lp
 * @version $Id: AbstractCodemaMachine.java, v 0.1 2017-08-24 3:32 dushang.lp Exp $
 */
public abstract class AbstractVoidCodemaMachine<T extends CommonCodemaConfig>
        extends AbstractBaseCodemaMachine<T, Object, Object> {

    @Override public void code(T config, Object source) throws Exception {
        codeEach(config);
    }

    protected abstract void codeEach(T config) throws Exception;

}