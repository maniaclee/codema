package com.lvbby.codema.app.testcase;

import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.config.ConfigKey;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;

import java.io.Serializable;

/**
 * Created by lipeng on 2016/12/22.
 */
@ConfigKey("java.testcase")
@ConfigBind(JavaTestcaseCodemaMachine.class)
public class JavaTestcaseCodemaConfig extends JavaBasicCodemaConfig implements Serializable {
    private static final long serialVersionUID = 7518066872925613331L;
    private boolean spring = false;

    public boolean isSpring() {
        return spring;
    }

    public void setSpring(boolean spring) {
        this.spring = spring;
    }
}
