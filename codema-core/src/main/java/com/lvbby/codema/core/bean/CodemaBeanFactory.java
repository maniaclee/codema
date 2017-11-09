package com.lvbby.codema.core.bean;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by lipeng on 2016/12/31.
 */
public interface CodemaBeanFactory {

    /**
     * ×¢Èëbean
     * @param resource
     */
    void register(CodemaBean resource);

    <T> T getBean(String id);

    <T> List<T> getBeans( Class<T> clz,Predicate<CodemaBean> predicate);

    <T> T getBean(Class<T> type);

    <T> List<T> getBeans(Class<T> type);
}
