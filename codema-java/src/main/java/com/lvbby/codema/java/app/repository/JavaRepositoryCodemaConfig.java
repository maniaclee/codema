package com.lvbby.codema.java.app.repository;

import com.lvbby.codema.core.config.ConfigKey;
import com.lvbby.codema.java.app.baisc.JavaBasicCodemaConfig;

/**
 * Created by lipeng on 2017/1/7.
 */
@ConfigKey("java.repository")
public class JavaRepositoryCodemaConfig extends JavaBasicCodemaConfig {
    private String convertUtilsClass;

    public String getConvertUtilsClass() {
        return convertUtilsClass;
    }

    public void setConvertUtilsClass(String convertUtilsClass) {
        this.convertUtilsClass = convertUtilsClass;
    }
}
