package com.lvbby.codema.core;

import java.util.Map;

/**
 *
 * @author dushang.lp
 * @version $Id: TypedKey.java, v 0.1 2017-11-28 下午6:02 dushang.lp Exp $
 */
public class TypedMap {
    private Map<TypedKey, Object> map;

    public <T> TypedMap put(TypedKey<T> key, T object) {
        map.put(key, object);
        return this;
    }

    public <T> T get(TypedKey<T> key) {
        return (T) map.get(key);
    }
}