package com.lvbby.codema.core.source;

import com.lvbby.codema.core.SourceParser;
import com.lvbby.codema.core.utils.CodemaUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;

/**
 * Created by lipeng on 17/1/4.
 */
public class FileSourceParser implements SourceParser<String> {
    public static final String schema = "file://";

    @Override
    public String getSupportedUriScheme() {
        return schema;
    }

    @Override
    public String parse(URI from) throws Exception {
        return IOUtils.toString(new FileInputStream(CodemaUtils.getResourcePath(from)));
    }

    public static String toURI(File file) {
        return schema + file.getAbsolutePath();
    }
}
