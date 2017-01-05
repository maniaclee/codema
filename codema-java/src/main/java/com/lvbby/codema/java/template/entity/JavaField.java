package com.lvbby.codema.java.template.entity;

/**
 * Created by lipeng on 17/1/5.
 */
public class JavaField {
    private String name;
    private String type;
    private boolean isPrimitive;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPrimitive() {
        return isPrimitive;
    }

    public void setPrimitive(boolean primitive) {
        isPrimitive = primitive;
    }
}
