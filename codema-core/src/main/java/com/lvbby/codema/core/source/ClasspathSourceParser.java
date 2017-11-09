package com.lvbby.codema.core.source;

import org.apache.commons.io.IOUtils;

/**
 * Created by lipeng on 17/1/4.
 */
public class ClasspathSourceParser extends SingleSourceLoader<String> {
    public ClasspathSourceParser(String resource) throws Exception {
        this(null, resource);
    }

    public ClasspathSourceParser(ClassLoader classLoader, String resource) throws Exception {
        if (classLoader == null) {
            classLoader = getClass().getClassLoader();
        }
        setSource(IOUtils.toString(classLoader.getResourceAsStream(resource)));
    }

}
