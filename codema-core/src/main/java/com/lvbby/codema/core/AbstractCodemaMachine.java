package com.lvbby.codema.core;

import com.lvbby.codema.core.result.Result;

/**
 * 可以处理结果
 * @author dushang.lp
 * @version $Id: AbstractCodemaMachine.java, v 0.1 2017-08-24 3:32 dushang.lp Exp $
 */
public abstract class AbstractCodemaMachine<S, O> extends AbstractBaseCodemaMachine<S, O> {

    @Override public void doCode() throws Exception {
        //调用handler处理
        handle(codeEach(source));
    }

    protected abstract Result<O> codeEach(S source) throws Exception;

}