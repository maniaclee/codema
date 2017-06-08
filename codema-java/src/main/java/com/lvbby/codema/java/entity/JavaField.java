package com.lvbby.codema.java.entity;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by lipeng on 17/1/5.
 */
public class JavaField {
    private String name;
    private JavaType type;
    private boolean isPrimitive;
    private List<JavaType> annotations;

    public static JavaField from(Field field){
        JavaField javaField = new JavaField();
        javaField.setName(field.getName());
        javaField.setType(JavaType.ofField(field));
        javaField.setPrimitive(field.getType().isPrimitive());
        javaField.setAnnotations(Stream.of(field.getAnnotations()).map(annotation -> JavaType.ofClass(annotation.annotationType())).collect(Collectors.toList()));
        return javaField;
    }

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
