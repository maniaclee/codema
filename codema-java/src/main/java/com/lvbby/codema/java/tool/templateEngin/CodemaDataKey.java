package com.lvbby.codema.java.tool.templateEngin;

import com.github.javaparser.ast.DataKey;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by dushang.lp on 2017/7/21.
 */
public class CodemaDataKey<T> extends DataKey<T> {
    private static AtomicInteger bigInteger = new AtomicInteger(100000);
    private String name;

    public static <T> CodemaDataKey<T> instance(String name) {
        return new CodemaDataKey<>(name);
    }

    public static <T> CodemaDataKey<T> instance() {
        return new CodemaDataKey<>("MyDataKey" + bigInteger.incrementAndGet());
    }

    private CodemaDataKey(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CodemaDataKey<?> myDataKey = (CodemaDataKey<?>) o;

        return name.equals(myDataKey.name);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
