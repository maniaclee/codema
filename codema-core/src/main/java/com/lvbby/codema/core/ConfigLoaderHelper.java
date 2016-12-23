package com.lvbby.codema.core;

/**
 * Created by lipeng on 16/12/22.
 */
public class ConfigLoaderHelper {

    public static ConfigLoader load(String code, Class<? extends ConfigLoader> clz) throws Exception {
        ConfigLoader configLoader = clz.newInstance();
        configLoader.load(code);
        return configLoader;
    }
}
