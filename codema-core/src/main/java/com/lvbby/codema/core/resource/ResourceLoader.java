package com.lvbby.codema.core.resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author dushang.lp
 * @version $Id: ResourceLoader.java, v 0.1 2018年03月07日 下午3:52 dushang.lp Exp $
 */
public class ResourceLoader {
    public static ResourceLoader instance = new ResourceLoader();

    public Resource load(String path) {
        Validate.notBlank(path);
        Matcher matcher = Pattern.compile("([a-zA-Z]+)://(.*)").matcher(path);
        Validate.isTrue(matcher.find(), "invalid resource path: %s", path);
        String protocol = matcher.group(1);
        String url = matcher.group(2);
        if (StringUtils.equalsIgnoreCase(protocol, "file")) {
            return new FileResource(new File(url));
        }
        if (StringUtils.equalsIgnoreCase(protocol, "classpath")) {
            return new ClassPathResource(url);
        }
        throw new IllegalArgumentException("invalid resource path,please use: file://filepath or classpath://classpath_resource");
    }
}