package com.lvbby.codema.core.source;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by lipeng on 17/1/4.
 */
public class FileSourceParser extends AbstractSourceLoader<String> {

    public FileSourceParser(File file) throws Exception {
        setInputStream(new FileInputStream(file));
    }

    @Override
    public String loadSource() throws Exception {
        return IOUtils.toString(inputStream);
    }
}
