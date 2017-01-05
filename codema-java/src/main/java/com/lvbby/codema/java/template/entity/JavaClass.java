package com.lvbby.codema.java.template.entity;

import java.util.List;

/**
 * Created by lipeng on 17/1/5.
 */
public class JavaClass {
    private String name;
    private List<JavaField> fields;
    private List<JavaMethod> methods;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JavaField> getFields() {
        return fields;
    }

    public void setFields(List<JavaField> fields) {
        this.fields = fields;
    }

    public List<JavaMethod> getMethods() {
        return methods;
    }

    public void setMethods(List<JavaMethod> methods) {
        this.methods = methods;
    }
}
