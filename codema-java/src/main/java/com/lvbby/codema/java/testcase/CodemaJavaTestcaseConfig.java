package com.lvbby.codema.java.testcase;

import com.lvbby.codema.java.config.CodemaJavaAbstractConfig;

import java.io.Serializable;

/**
 * Created by lipeng on 2016/12/22.
 */
//@ConfigKey("java.testcase")
public class CodemaJavaTestcaseConfig extends CodemaJavaAbstractConfig implements Serializable {
    private boolean spring = false;

    public boolean isSpring() {
        return spring;
    }

    public void setSpring(boolean spring) {
        this.spring = spring;
    }
}
