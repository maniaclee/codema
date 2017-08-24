package com.lvbby.codema.core.handler;

import com.lvbby.codema.core.ResultContext;
import com.lvbby.codema.core.result.AbstractResultHandler;
import com.lvbby.codema.core.result.FileResult;
import com.lvbby.codema.core.result.PrintableResult;
import com.lvbby.codema.core.utils.ReflectionUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by lipeng on 17/1/4.
 */
public class FileWriterResultHandler extends AbstractResultHandler<FileResult> {

    @Override
    protected void process(ResultContext resultContext, FileResult result) throws Exception {
        File file = result.getFile();
        if (file == null)
            return;
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            ReflectionUtils.makeSureDir(file.getParentFile());
            logger.debug("mkdir : %s ", file.getParentFile());
        }
        IOUtils.write(getContent(result, file, resultContext), new FileOutputStream(file));
        logger.debug("write file: %s",file);
    }

    /**
     * 获取准备要写入的内容
     *
     * @param result        结果
     * @param destFile      要写入的文件
     * @param resultContext
     * @return
     */
    protected String getContent(PrintableResult result, File destFile,
                                ResultContext resultContext) throws Exception {
        return result.getString();
    }
}
