package com.lvbby.codema.java.testcase;

import com.lvbby.codema.java.baisc.CodemaJavaBasicConfig;

import java.io.Serializable;

/**
 * Created by lipeng on 2016/12/22.
 */
//@ConfigKey("java.testcase")
public class CodemaJavaTestcaseConfig extends CodemaJavaBasicConfig implements Serializable {
    private boolean spring = false;

    public boolean isSpring() {
        return spring;
    }

    public void setSpring(boolean spring) {
        this.spring = spring;
    }
}
