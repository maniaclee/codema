package com.lvbby.codema.core;

import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.result.Result;

import java.util.List;

/**
 * Created by lipeng on 16/12/23.
 * 代码机的处理器，只处理
 */
public interface CodemaMachine<S, O> {

    /***
     * 设置source
     * @param source
     * @return
     */
    CodemaMachine<S, O> source(S source);

    /***
     * 设置handlers
     * @param handlers
     * @return
     */
    CodemaMachine<S, O> resultHandlers(List<ResultHandler> handlers);



    void code() throws Exception;

    Result<O> getResult();

    /**
     * 连接下一个CodemaMachine
     * @param next
     */
     CodemaMachine< S, O> next(CodemaMachine next);
    /**
     * 连接下一个CodemaMachine,类型必须一致
     * @param next
     */
     <Output> CodemaMachine<S, O> nextWithCheck(CodemaMachine<O,Output> next);

    /***
     * 获取source类型
     * @return
     */
    Class<S> sourceType();

    /** 获取输出类型*/
    Class<O> outputType();


}
