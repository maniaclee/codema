package com.lvbby.codema.app.testcase.mock;

import com.lvbby.codema.java.entity.JavaField;
import com.lvbby.codema.java.entity.JavaMethod;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;

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

    public String parseMockSentence() {
        String collect = method.getArgs().stream().map(javaArg -> String.format("Mockito.any(%s.class)", javaArg.getType().getName())).collect(Collectors.joining(","));
        return String.format("Mockito.when(%s.%s(%s)).thenReturn(%s);"
                , StringUtils.uncapitalize(javaField.getType().getName())
                , method.getName()
                , collect
                , "null"
        );
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

        return method.equals(that.method);
    }

    @Override
    public int hashCode() {
        return method.hashCode();
    }
}
