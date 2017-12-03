package com.lvbby.codema.core;

import com.lvbby.codema.core.bean.CodemaBean;

/**
 * Created by lipeng on 2017/1/18.
 */
public class CodemaContextHolder {
    private static ThreadLocal<CodemaContext> beanFactoryThreadLocal = ThreadLocal
            .withInitial(() -> new CodemaContext());

    public static void put(CodemaBean bean) {
        if(bean==null) {
            return;
        }
        beanFactoryThreadLocal.get().getCodemaBeanFactory().register(bean);
    }
    public static CodemaContext get(){
        return beanFactoryThreadLocal.get();
    }

    public static void clear() {
        beanFactoryThreadLocal.remove();
    }
}
