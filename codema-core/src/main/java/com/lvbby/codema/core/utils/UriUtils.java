package com.lvbby.codema.core.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.lvbby.codema.core.error.CodemaRuntimeException;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.Collection;

/**
 * Created by lipeng on 2017/1/8.
 */
public class UriUtils {

    public static Multimap<String, String> getQueryParameterMap(URI uri) {
        Multimap<String, String> map = ArrayListMultimap.create();
        if (StringUtils.isBlank(uri.getQuery()))
            return map;
        for (String s : uri.getQuery().split("&")) {
            String[] split = s.split("=");
            if (split.length == 0)
                continue;
            String value = split.length >= 2 ? split[1] : "";
            map.put(split[0], value);
        }
        return map;
    }

    public static String getQueryParameter(URI uri, String parameter) {
        Collection<String> strings = getQueryParameterMap(uri).get(parameter);
        if (strings.size() > 1)
            throw new CodemaRuntimeException(String.format("multi value found for parameter % in url : %s", parameter, uri));
        return strings.iterator().next();
    }

    public static Collection<String> getQueryParameters(URI uri, String parameter) {
        return getQueryParameterMap(uri).get(parameter);
    }
}
