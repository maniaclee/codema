package com.lvbby.codema.core.config;

/**
 * Created by lipeng on 16/12/22.
 */
public interface ConfigLoader {

    <T> T getConfig(Class<T> clz);
}
