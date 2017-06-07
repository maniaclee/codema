package com.lvbby.codema.java.entity;

import java.util.List;

/**
 * Created by lipeng on 17/1/5.
 */
public class JavaField {
    private String name;
    private JavaType type;
    private boolean isPrimitive;
    private List<JavaType> annotations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JavaType getType() {
        return type;
    }

    public void setType(JavaType type) {
        this.type = type;
    }

    public boolean isPrimitive() {
        return isPrimitive;
    }

    public void setPrimitive(boolean primitive) {
        isPrimitive = primitive;
    }

    public List<JavaType> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<JavaType> annotations) {
        this.annotations = annotations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JavaField javaField = (JavaField) o;

        if (!name.equals(javaField.name)) return false;
        return type.equals(javaField.type);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
