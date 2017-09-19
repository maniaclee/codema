package com.lvbby.codema.core.handler;

import com.lvbby.codema.core.ResultContext;
import com.lvbby.codema.core.result.FileResult;
import com.lvbby.codema.core.result.MergeCapableFileResult;
import com.lvbby.codema.core.utils.ReflectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 支持各种模式的文件写入
 * Created by lipeng on 17/1/4.
 */
public class FileWriterResultHandler extends AbstractResultHandler<FileResult> {

    public final static String write_mode_direct = "direct";
    public final static String write_mode_append = "append";
    public final static String write_mode_merge = "merge";
    private String writeMode = write_mode_direct;

    public FileWriterResultHandler(String writeMode) {
        this.writeMode = writeMode;
    }

    public FileWriterResultHandler() {
    }

    @Override
    protected void process(ResultContext resultContext, FileResult result) throws Exception {
        File file = result.getFile();
        if (file == null)
            return;
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            ReflectionUtils.makeSureDir(file.getParentFile());
            logger.debug("mkdir : %s ", file.getParentFile());
        }
        //直接写
        if (!file.exists()) {
            write(result.getString(), new FileOutputStream(file), file);
            return;
        }
        Validate.isTrue(file.isFile(), "dest file is not a file but directory %s", file.getAbsolutePath());
        //已经存在，根据指定模式写
        switch (writeMode) {
            case write_mode_direct:
                write(result.getString(), new FileOutputStream(file), file);
                return;
            case write_mode_append:
                write(result.getString(), new FileOutputStream(file, true), file);
                return;
            case write_mode_merge:
                //不支持merge，用覆写的形式
                if(!(result instanceof MergeCapableFileResult)){
                    write(result.getString(), new FileOutputStream(file), file);
                    return;
                }
                Validate.isTrue(result instanceof MergeCapableFileResult, "result[%s] doesn't support merge mode for file:%s", result.getClass().getName(), result.getFile());
                FileInputStream dest = new FileInputStream(file);
                String s = ((MergeCapableFileResult) result).parseMergeResult(dest, resultContext);
                IOUtils.closeQuietly(dest);
                write(s, new FileOutputStream(file), file);
                return;
            default:
                throw new IllegalArgumentException(String.format("unsupported write mode for : %s", writeMode));
        }
    }

    private void write(String result, OutputStream outputStream, File file) throws IOException {
        IOUtils.write(result, outputStream);
        logger.debug("write file: %s, mode[%s]", file, writeMode);
    }

    public String getWriteMode() {
        return writeMode;
    }

    public void setWriteMode(String writeMode) {
        this.writeMode = writeMode;
    }
}
