package com.lvbby.codema.core;

import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.result.Result;

/**
 * Created by lipeng on 16/12/23.
 * 代码机的处理器，只处理
 */
public interface CodemaMachine<T extends CommonCodemaConfig, S, O> {

    void code(T config, S source) throws Exception;

    Result<O> getResult();
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
