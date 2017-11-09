package com.lvbby.codema.core.resource;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;

/**
 *
 * @author dushang.lp
 * @version $Id: FileResource.java, v 0.1 2017-11-09 下午11:03 dushang.lp Exp $
 */
public class ClassPathResource extends AbstractNamedResource {
    private String      classPath;
    private ClassLoader classLoader;

    public ClassPathResource(String classPath) {
        this.classPath = classPath;
        int i = classPath.lastIndexOf("/");
        if (i < 0) {
            i = 0;
        }
        setResourceName(StringUtils.substring(classPath,i));
    }

    public ClassPathResource classLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    @Override public InputStream getInputStream() throws Exception {
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        return classLoader.getResourceAsStream(classPath);
    }
}