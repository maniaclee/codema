package com.lvbby.codema.core.machine;

import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.result.BasicResult;

/**
 * 用来当做source的machine模板
 * @author dushang.lp
 * @version $Id: JavaFromSrcMachine.java, v 0.1 2018年01月19日 下午6:41 dushang.lp Exp $
 */
public abstract class AbstractSourceMachine<S, O> extends AbstractBaseMachine<S, O> {
    @Override
    protected void doCode() throws Exception {
        handle(BasicResult.instance(genSource(source)));
    }

    public abstract O genSource(S source) throws Exception;
}