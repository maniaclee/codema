package com.lvbby.codema.core.utils;

import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.Machine;
import com.lvbby.codema.core.result.Result;

import java.util.function.Consumer;

/**
 *
 * @author dushang.lp
 * @version $Id: CodemaMachineUtils.java, v 0.1 2017-12-02 下午9:56 dushang.lp Exp $
 */
public class CodemaMachineUtils {
    public static <S, O> Machine<S, O> build(FunctionAdaptor<S, Result<O>> functionAdaptor) {
        return new AbstractBaseMachine<S, O>() {
            @Override protected void doCode() throws Exception {
//                setResult(functionAdaptor.apply(this.source));
                handle(functionAdaptor.apply(this.source));
            }
        };
    }
    public static <S, O> Machine<S, O> buildUsingConsumer(Consumer<S> functionAdaptor) {
        return new AbstractBaseMachine<S, O>() {
            @Override protected void doCode() throws Exception {
                functionAdaptor.accept(source);
            }
        };
    }
}