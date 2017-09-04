package com.lvbby.codema.app.testcase.mock;

import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dushang.lp on 2017/5/31.
 */
@ConfigBind(JavaMockTestCodemaMachine.class)
public class JavaMockTestCodemaConfig extends JavaBasicCodemaConfig implements Serializable {
    private static final long serialVersionUID = -5605104495541164985L;
    /***
     *   the dependency annotations
     */
    private List<String> dependencyAnnotation;

    public List<String> getDependencyAnnotation() {
        return dependencyAnnotation;
    }

    public void setDependencyAnnotation(List<String> dependencyAnnotation) {
        this.dependencyAnnotation = dependencyAnnotation;
    }
}
