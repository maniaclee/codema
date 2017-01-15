package com.lvbby.codema.core.result;

import com.lvbby.codema.core.utils.ReflectionUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by lipeng on 17/1/9.
 */
public class BasicResult implements PrintableResult, FileResult {

    private Object result;
    private File file;

    public BasicResult result(Object result) {
        this.result = result;
        return this;
    }

    public BasicResult() {
    }

    public BasicResult(File file) {
        this(null, file);
    }

    /**
     * 从一个类的package内获取资源，包装成一个FileResult
     */
    public static Result ofResource(Class clz, String resourceName, String destDir) throws IOException {
        return new BasicResult(ReflectionUtils.loadResource(clz, resourceName), new File(destDir, resourceName));
    }

    public BasicResult(Object result, File file) {
        this.result = result;
        this.file = file;
    }

    @Override
    public Object getResult() {
        return result;
    }

    @Override
    public String getString() {
        return result == null ? "" : result.toString();
    }

    @Override
    public File getFile() {
        return file;
    }
}
