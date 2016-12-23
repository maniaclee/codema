package com.lvbby.codema.core;

/**
 * Created by lipeng on 16/12/23.
 */
public class CodemaContext {
    private ConfigLoader configLoader;

    public <T> T getConfig(Class<T> clz) {
        return configLoader.getConfig(clz);
    }

    public boolean hasConfig(Class clz) {
        return getConfig(clz) != null;
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    public void setConfigLoader(ConfigLoader configLoader) {
        this.configLoader = configLoader;
    }


}
