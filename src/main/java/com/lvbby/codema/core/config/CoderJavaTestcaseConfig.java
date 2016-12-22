package com.lvbby.codema.core.config;

import java.io.Serializable;

/**
 * Created by lipeng on 2016/12/22.
 */
//@ConfigKey("java.testcase")
public class CoderJavaTestcaseConfig extends CoderJavaAbstractConfig implements Serializable {
    private boolean spring = false;

    public boolean isSpring() {
        return spring;
    }

    public void setSpring(boolean spring) {
        this.spring = spring;
    }
}
