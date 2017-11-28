package com.lvbby.codema.core;

import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.result.Result;

/**
 * Created by lipeng on 16/12/23.
 * 代码机的处理器，只处理
 */
public interface CodemaMachine<T extends CommonCodemaConfig, S, O> {

    <T> T getArg(TypedKey<T> key);

    void code(S source) throws Exception;

    Result<O> getResult();

    /**
     * 连接下一个CodemaMachine
     * @param next
     */
     CodemaMachine<T , S, O> next(CodemaMachine next);
    /**
     * 连接下一个CodemaMachine,类型必须一致
     * @param next
     */
     <ConfigOther extends CommonCodemaConfig,Output> CodemaMachine<T , S, O> nextWithCheck(CodemaMachine<ConfigOther,O,Output> next);

    T getConfig();

    CodemaMachine<T , S, O> setConfig(T config);

    /***
     * 获取绑定的config类型
     * @return
     */
    Class<T> getConfigType();
    /***
     * 获取source类型
     * @return
     */
    Class<S> getSourceType();

    /** 获取输出类型*/
    Class<O> getDestType();


}
