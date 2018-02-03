package com.lvbby.codema.core.utils;

/**
 *
 * @author dushang.lp
 * @version $Id: CodemaCacheHolder.java, v 0.1 2017年12月18日 下午1:42 dushang.lp Exp $
 */
public class CodemaCacheHolder {
    public static CodemaCache cache = CodemaCache.instance;

    public static CodemaCache getCache() {
        return cache;
    }
}

