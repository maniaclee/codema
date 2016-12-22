package com.lvbby.codema.core.config;

/**
 * Created by lipeng on 2016/12/22.
 */
public abstract class CoderJavaAbstractConfig {
    private String from;
    private String destClassSuffix;
    private String destPack;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDestClassSuffix() {
        return destClassSuffix;
    }

    public void setDestClassSuffix(String destClassSuffix) {
        this.destClassSuffix = destClassSuffix;
    }

    public String getDestPack() {
        return destPack;
    }

    public void setDestPack(String destPack) {
        this.destPack = destPack;
    }
}
