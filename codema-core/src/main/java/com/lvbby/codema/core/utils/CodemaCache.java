package com.lvbby.codema.core.utils;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 *
 * @author dushang.lp
 * @version $Id: CodemaCache.java, v 0.1 2017年12月18日 下午1:39 dushang.lp Exp $
 */
public interface CodemaCache {
    CodemaCache instance = new CodemaCache() {
        private Map<String,Object> map = Maps.newConcurrentMap();
        @Override public void put(String key, Object value) {
            map.put(key,value);
        }

        @Override public Object get(String key) {
            return map.get(key);
        }
    };
    void put(String key , Object value);

    Object get(String key);
}