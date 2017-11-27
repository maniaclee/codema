package com.lvbby.codema.app.javaMdDoc;

import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;

/**
 * Created by dushang.lp on 2017/8/16.
 */
@ConfigBind(JavaMdDocInterfaceCodemaMachine.class)
public class JavaMdDocCodemaConfig extends JavaBasicCodemaConfig {
    private String method;
    /** 全类名 */
    private String signiture;
    /**
     * 标题的级别，默认是2级标题
     */
    private int    titleLevel = 2;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getTitleLevel() {
        return titleLevel;
    }

    public void setTitleLevel(int titleLevel) {
        this.titleLevel = titleLevel;
    }

    /**
     * Getter method for property   signiture.
     *
     * @return property value of signiture
     */
    public String getSigniture() {
        return signiture;
    }

    /**
     * Setter method for property   signiture .
     *
     * @param signiture  value to be assigned to property signiture
     */
    public void setSigniture(String signiture) {
        this.signiture = signiture;
    }
}
