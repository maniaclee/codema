package com.lvbby.codema.java.baisc;

import com.lvbby.codema.core.ConfigKey;

import java.io.Serializable;

/**
 * Created by lipeng on 2016/12/22.
 */
@ConfigKey("java.basic")
public class CodemaJavaBasicConfig implements Serializable {
    /** 目标package */
    private String pack;
    /** 目标src路径 */
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
