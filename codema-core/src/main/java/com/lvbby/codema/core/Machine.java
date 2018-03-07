package com.lvbby.codema.core;

import com.lvbby.codema.core.result.Result;

import java.util.List;

/**
 * Created by lipeng on 16/12/23.
 * 代码机的处理器，只处理
 */
public interface Machine<S, O> {

    /***
     * 设置source
     * @param source
     * @return
     */
    Machine<S, O> source(S source);

    S getSource();

    /***
     * 设置handlers
     * @param handlers
     * @return
     */
    Machine<S, O> resultHandlers(List<ResultHandler> handlers);

    Machine<S, O> addResultHandler(ResultHandler handler);

    /***
     * 运行
     * @throws Exception
     */
    void run() throws Exception;

    Result<O> getResult();

    Machine<S, O> depend(Machine supplier);

    <T> T getDependency();

    /**
     * 连接下一个CodemaMachine
     * @param next
     */
    Machine<S, O> next(Machine next);

    /**
     * 连接下一个CodemaMachine,类型必须一致
     * @param next
     */
    default <Output> Machine<S, O> nextWithCheck(Machine<O, Output> next) {
        next(next);
        return this;
    }

    /***
     * 获取source类型
     * @return
     */
    Class<S> sourceType();

    /** 获取输出类型*/
    Class<O> outputType();

}
