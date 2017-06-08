package com.lvbby.codema.java.entity;

import com.lvbby.codema.java.tool.JavaLexer;

/**
 * Created by lipeng on 17/1/5.
 */
public class JavaArg {
    private String name;
    private JavaType type;

    public String getDefaultValue() {
        return JavaLexer.newInstanceForDefaultValue(type.getName()).toString();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JavaArg javaArg = (JavaArg) o;

        if (!name.equals(javaArg.name)) return false;
        return type.equals(javaArg.type);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s %s",type,name);
    }
}
