package com.lvbby.codema.core.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 *
 * @author dushang.lp
 * @version $Id: FileResource.java, v 0.1 2017-11-09 下午11:03 dushang.lp Exp $
 */
public class FileResource extends AbstractNamedResource {
    private File file;

    public FileResource(File file) {
        this.file = file;
        setResourceName(file.getName());
    }

    @Override
    public InputStream getInputStream() throws Exception {
        return new FileInputStream(file);
    }
}