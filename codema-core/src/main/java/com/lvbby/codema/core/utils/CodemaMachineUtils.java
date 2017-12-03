package com.lvbby.codema.core.utils;

import com.lvbby.codema.core.AbstractBaseCodemaMachine;
import com.lvbby.codema.core.CodemaMachine;
import com.lvbby.codema.core.result.Result;

/**
 *
 * @author dushang.lp
 * @version $Id: CodemaMachineUtils.java, v 0.1 2017-12-02 下午9:56 dushang.lp Exp $
 */
public class CodemaMachineUtils {
    public static <S, O> CodemaMachine<S, O> build(FunctionAdaptor<S, Result<O>> functionAdaptor) {
        return new AbstractBaseCodemaMachine<S, O>() {
            @Override protected void doCode() throws Exception {
                setResult(functionAdaptor.apply(this.source));
            }
        };
    }
}