package com.lvbby.codema.java.entity;

import com.lvbby.codema.core.utils.ReflectionUtils;

import java.util.List;

/**
 * Created by lipeng on 17/1/5.
 */
public class JavaClass {
    private String pack;
    private String name;
    private List<JavaField> fields;
    private List<JavaMethod> methods;
    /**
     * 来源于什么对象
     */
    private Object from;

    public String getNameCamel() {
        return ReflectionUtils.camel(name);
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

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

    public Object getFrom() {
        return from;
    }

    public void setFrom(Object from) {
        this.from = from;
    }
}
