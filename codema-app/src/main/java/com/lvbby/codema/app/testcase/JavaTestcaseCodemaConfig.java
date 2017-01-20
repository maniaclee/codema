package com.lvbby.codema.app.testcase;

import com.lvbby.codema.core.config.ConfigKey;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;

import java.io.Serializable;

/**
 * Created by lipeng on 2016/12/22.
 */
@ConfigKey("java.testcase")
public class JavaTestcaseCodemaConfig extends JavaBasicCodemaConfig implements Serializable {
    private boolean spring = false;

    public boolean isSpring() {
        return spring;
    }

    public void setSpring(boolean spring) {
        this.spring = spring;
    }
}
