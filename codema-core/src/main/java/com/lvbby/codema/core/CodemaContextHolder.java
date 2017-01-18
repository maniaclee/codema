package com.lvbby.codema.core;

/**
 * Created by lipeng on 2017/1/18.
 */
public class CodemaContextHolder {
    private static ThreadLocal<CodemaContext> codemaContextThreadLocal = new ThreadLocal<>();

    public static CodemaContext getCodemaContext() {
        return codemaContextThreadLocal.get();
    }

    public static void setCodemaContext(CodemaContext codemaContext) {
        codemaContextThreadLocal.set(codemaContext);
    }

    public static void clear() {
        codemaContextThreadLocal.remove();
    }

}
