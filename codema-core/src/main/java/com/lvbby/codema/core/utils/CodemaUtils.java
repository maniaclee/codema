package com.lvbby.codema.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.net.URI;

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
}
