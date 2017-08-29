package com.lvbby.codema.app.repository;

import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;

/**
 * Created by lipeng on 2017/1/7.
 */
@ConfigBind(JavaRepositoryCodemaMachine.class)
public class JavaRepositoryCodemaConfig extends JavaBasicCodemaConfig {
    private String convertUtilsClass = "BuildUtils";

    public String getConvertUtilsClass() {
        return convertUtilsClass;
    }

    public void setConvertUtilsClass(String convertUtilsClass) {
        this.convertUtilsClass = convertUtilsClass;
    }
}
