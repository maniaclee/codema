package com.lvbby.codema.java.app.convert;

import com.lvbby.codema.core.config.ConfigKey;
import com.lvbby.codema.java.app.baisc.JavaBasicCodemaConfig;

/**
 * Created by lipeng on 16/12/27.
 */
@ConfigKey("java.convert")
public class JavaConvertCodemaConfig extends JavaBasicCodemaConfig {
    private String convertToClassName;

    public String getConvertToClassName() {
        return convertToClassName;
    }

    public void setConvertToClassName(String convertToClassName) {
        this.convertToClassName = convertToClassName;
    }
}
