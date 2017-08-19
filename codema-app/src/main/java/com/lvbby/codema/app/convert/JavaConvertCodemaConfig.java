package com.lvbby.codema.app.convert;

import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;

/**
 * Created by lipeng on 16/12/27.
 */
@ConfigBind(JavaConvertCodemaMachine.class)
public class JavaConvertCodemaConfig extends JavaBasicCodemaConfig {
    private String convertToClassName;

    public String getConvertToClassName() {
        return convertToClassName;
    }

    public void setConvertToClassName(String convertToClassName) {
        this.convertToClassName = convertToClassName;
    }
}
