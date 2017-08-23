package com.lvbby.codema.core.source;

import com.lvbby.codema.core.SourceParser;
import com.lvbby.codema.core.utils.CodemaUtils;
import org.apache.commons.io.IOUtils;

import java.net.URI;

/**
 * Created by lipeng on 17/1/4.
 */
public class ClasspathSourceParser extends AbstractSourceLoader<String> {

    public ClasspathSourceParser(String resource) throws Exception {
        this(null, resource);
    }

    public ClasspathSourceParser(ClassLoader classLoader, String resource) throws Exception {
        if (classLoader == null) {
            classLoader = getClass().getClassLoader();
        }
        setInputStream(classLoader.getResourceAsStream(resource));
    }

    @Override
    public String loadSource() throws Exception {
        return IOUtils.toString(inputStream);
    }
}
