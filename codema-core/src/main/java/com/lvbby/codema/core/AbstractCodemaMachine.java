package com.lvbby.codema.core;

import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.result.Result;

/**
 * 可以处理结果
 * @author dushang.lp
 * @version $Id: AbstractCodemaMachine.java, v 0.1 2017-08-24 3:32 dushang.lp Exp $
 */
public abstract class AbstractCodemaMachine<T extends CommonCodemaConfig,S,O>
                                           extends AbstractBaseCodemaMachine<T,S,O> {

    @Override public void code(T config, S source ) throws Exception {
        //result
        Result<O> re = codeEach(config,source);
        //设置result
        setResult(re);
        //调用handler处理
        config.handle(re);
    }

    protected abstract Result<O> codeEach(T config, S source) throws Exception;

}