package com.lvbby.codema.app.convert;

import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.config.NotBlank;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.baisc.JavaTemplateResource;

/**
 * Created by lipeng on 16/12/27.
 */
@JavaTemplateResource($Convert_.class)
public class JavaConvertCodemaConfig extends JavaBasicCodemaConfig {
    @NotBlank
    private String convertToClassName;


    public String getConvertToClassName() {
        return convertToClassName;
    }

    public void setConvertToClassName(String convertToClassName) {
        this.convertToClassName = convertToClassName;
    }
}
