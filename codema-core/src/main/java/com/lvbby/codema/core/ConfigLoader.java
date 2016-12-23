package com.lvbby.codema.core;

/**
 * Created by lipeng on 16/12/22.
 */
public interface ConfigLoader {

    void load(String code) throws Exception;

    <T> T getConfig(Class<T> clz);
}
