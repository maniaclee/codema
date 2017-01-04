package com.lvbby.codema.core.parser;

import com.lvbby.codema.core.SourceParser;
import com.lvbby.codema.core.utils.CodemaUtils;
import org.apache.commons.io.IOUtils;

import java.net.URI;

/**
 * Created by lipeng on 17/1/4.
 */
public class ClasspathSourceParser implements SourceParser<String> {
    private ClassLoader classLoader;

    public ClasspathSourceParser(ClassLoader classLoader) {
        if (classLoader == null)
            classLoader = this.getClass().getClassLoader();
        this.classLoader = classLoader;
    }

    public ClasspathSourceParser() {
        this(null);
    }

    @Override
    public String getSupportedUriScheme() {
        return "classpath://";
    }

    @Override
    public String parse(URI from) throws Exception {
        return IOUtils.toString(classLoader.getResourceAsStream(CodemaUtils.getResourcePath(from)));
    }



}
