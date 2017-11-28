package com.lvbby.codema.core;

/**
 *
 * @author dushang.lp
 * @version $Id: TypedKey.java, v 0.1 2017-11-28 下午6:02 dushang.lp Exp $
 */
public class TypedKey<T> {
    private final String key;

    public TypedKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        TypedKey<?> typedKey = (TypedKey<?>) o;

        return key.equals(typedKey.key);
    }

    @Override public int hashCode() {
        return key.hashCode();
    }
}