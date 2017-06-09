package com.lvbby.codema.app.testcase.mock;

import com.lvbby.codema.java.entity.JavaField;
import com.lvbby.codema.java.entity.JavaMethod;

/**
 * Created by dushang.lp on 2017/6/9.
 */
public class MockDependencyMethod {
    private JavaField javaField;
    private JavaMethod method;

    public MockDependencyMethod(JavaField javaField, JavaMethod method) {
        this.javaField = javaField;
        this.method = method;
    }

    public JavaField getJavaField() {
        return javaField;
    }

    public void setJavaField(JavaField javaField) {
        this.javaField = javaField;
    }

    public JavaMethod getMethod() {
        return method;
    }

    public void setMethod(JavaMethod method) {
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MockDependencyMethod that = (MockDependencyMethod) o;

        if (!javaField.equals(that.javaField)) return false;
        return method.equals(that.method);
    }

    @Override
    public int hashCode() {
        int result = javaField.hashCode();
        result = 31 * result + method.hashCode();
        return result;
    }
}
