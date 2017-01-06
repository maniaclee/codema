package com.lvbby.codema.java.app.baisc;

import com.google.common.collect.Lists;
import com.lvbby.codema.java.entity.JavaClass;

import java.util.List;

/**
 * Created by lipeng on 2016/12/24.
 */
public class JavaSourceParam {
    private List<JavaClass> classes = Lists.newArrayList();

    public JavaSourceParam add(JavaClass compilationUnit) {
        classes.add(compilationUnit);
        return this;
    }

    public List<JavaClass> getClasses() {
        return classes;
    }

    public void setClasses(List<JavaClass> classes) {
        this.classes = classes;
    }
}
