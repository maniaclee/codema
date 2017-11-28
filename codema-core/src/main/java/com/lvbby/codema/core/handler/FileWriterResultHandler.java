package com.lvbby.codema.core.handler;

import com.lvbby.codema.core.result.FileResult;
import com.lvbby.codema.core.result.MergeCapableFileResult;
import com.lvbby.codema.core.result.WriteMode;
import com.lvbby.codema.core.utils.ReflectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
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

    private WriteMode writeMode;

    public FileWriterResultHandler(WriteMode writeMode) {
        this.writeMode = writeMode;
    }

    public FileWriterResultHandler() {
    }

    @Override protected void process(FileResult result)
            throws Exception {
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
        Validate.isTrue(file.isFile(), "dest file is not a file but directory %s",
                file.getAbsolutePath());
        //优先级: handler--> file --> default
        WriteMode writeMode = ObjectUtils
                .firstNonNull(this.writeMode,result.getWriteMode(),  WriteMode.flush);
        //已经存在，根据指定模式写
        switch (writeMode) {
            case flush:
                write(result.getString(), new FileOutputStream(file), file);
                return;
            case append:
                write(result.getString(), new FileOutputStream(file, true), file);
                return;
            case writeIfNoExist:
                return;
            case merge:
                //不支持merge，用覆写的形式
                if (!(result instanceof MergeCapableFileResult)) {
                    write(result.getString(), new FileOutputStream(file), file);
                    return;
                }
                Validate.isTrue(result instanceof MergeCapableFileResult,
                        "result[%s] doesn't support merge mode for file:%s",
                        result.getClass().getName(), result.getFile());
                FileInputStream dest = new FileInputStream(file);
                String s = ((MergeCapableFileResult) result).parseMergeResult(dest);
                IOUtils.closeQuietly(dest);
                write(s, new FileOutputStream(file), file);
                return;
            default:
                throw new IllegalArgumentException(
                        String.format("unsupported write mode for : %s", this.writeMode));
        }
    }

    private void write(String result, OutputStream outputStream, File file) throws IOException {
        IOUtils.write(result, outputStream);
        logger.debug("write file: %s, mode[%s]", file, writeMode);
    }

}
