package com.lvbby.codema.core.utils;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.config.RecursiveConfigField;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Created by lipeng on 17/1/4.
 */
public class CodemaUtils {
    public static String getResourcePath(URI uri) {
        String path = uri.getPath();
        if (StringUtils.isNotBlank(uri.getAuthority()))
            path = uri.getAuthority() + path;
        return path;
    }

    /***
     * 去掉path 开头的 /
     * @param uri
     * @return
     */
    public static String getPathPart(URI uri) {
        return uri.getPath().replaceFirst("/", "");
    }


    /***
     * 当配置是递归结构时，先序返回所有config
     * @param config
     * @param <T>
     * @return
     */
    public static <T extends CommonCodemaConfig> Collection<T> getAllConfigWithAnnotation(T config) {
        return getAllConfig(config, t -> ReflectionUtils.getFieldWithAnnotation(t, RecursiveConfigField.class));
    }

    public static <T extends CommonCodemaConfig> Collection<T> getAllConfig(T config, Function<T, Collection<T>> function) {
        List<T> re = Lists.newArrayList();
        getAllConfig(config, function, re);
        return re;
    }

    private static <T extends CommonCodemaConfig> void getAllConfig(T config, Function<T, Collection<T>> function, Collection<T> result) {
        if (config == null)
            return;
        result.add(config);
        if (function != null) {
            Collection<T> children = function.apply(config);
            if (CollectionUtils.isNotEmpty(children))
                children.forEach(t -> {
                    //如果children的值为null，继承父节点的值
                    ReflectionUtils.copyIfNull(t, config);
                    getAllConfig(t, function, result);
                });
        }
    }
}
