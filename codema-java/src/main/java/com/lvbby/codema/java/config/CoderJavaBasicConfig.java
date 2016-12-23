package com.lvbby.codema.java.config;

import com.lvbby.codema.core.ConfigKey;

import java.io.Serializable;

/**
 * Created by lipeng on 2016/12/22.
 */
@ConfigKey("java.basic")
public class CoderJavaBasicConfig implements Serializable {
    private String pack;
    private String srcRoot;

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getSrcRoot() {
        return srcRoot;
    }

    public void setSrcRoot(String srcRoot) {
        this.srcRoot = srcRoot;
    }
}
