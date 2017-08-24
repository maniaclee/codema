package com.lvbby.codema.core.result;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.utils.FileUtils;
import com.lvbby.codema.core.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lipeng on 17/1/9.
 */
public class BasicResult<T> implements PrintableResult, FileResult {

    /***
     * result的原始对象
     */
    private T            result;
    /***
     * 目标文件或目标路径路径，这里由几部分组成，方便调用方分多个部分组装
     */
    private List<String> filePaths = Lists.newLinkedList();
    /**目标文件*/
    private String       destFile;


    public BasicResult config(CommonCodemaConfig config){
        filePath(config.getDestRootDir());
        return this;
    }
    public BasicResult filePath(String... paths) {
        if (paths != null && paths.length > 0) {
            for (String path : paths) {
                filePaths.add(path);
            }
        }
        return this;
    }

    public BasicResult destFile(String file) {
        this.destFile = file;
        return this;
    }

    public BasicResult result(T result) {
        this.result = result;
        return this;
    }

    /**
     * 从一个类的package内获取资源，包装成一个FileResult
     */
    public static Result ofResource(Class clz, String resourceName) throws IOException {
        return new BasicResult().result(ReflectionUtils.loadResource(clz, resourceName));
    }

    @Override
    public T getResult() {
        return result;
    }

    @Override
    public String getString() {
        return result == null ? "" : result.toString();
    }

    @Override
    public File getFile() {
        for (String filePath : filePaths) {
            if (StringUtils.isBlank(filePath)) {
                throw new IllegalArgumentException(
                    String.format("invalid file paths:  %s", filePath));
            }
        }
        List<String> destPaths = Lists.newArrayList(filePaths);
        //加入目标文件
        if (StringUtils.isNotBlank(destFile)) {
            destPaths.add(destFile);
        }
        return FileUtils.parseFile(destPaths.toArray(new String[0]));
    }
}
