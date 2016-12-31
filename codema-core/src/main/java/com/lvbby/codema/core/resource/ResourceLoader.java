package com.lvbby.codema.core.resource;

import java.util.List;

/**
 * Created by lipeng on 2016/12/31.
 */
public interface ResourceLoader {
    void register(CodemaResource resource);

    <T> T getBean(String id);

    <T> T getBean(Class<T> type);

    <T> List<T> getBeans(Class<T> type);
}
