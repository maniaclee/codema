package com.lvbby.codema.core.result.handler;

import com.lvbby.codema.core.ResultContext;
import com.lvbby.codema.core.ResultHandler;
import com.lvbby.codema.core.result.FileResult;
import com.lvbby.codema.core.result.PrintableResult;
import com.lvbby.codema.core.utils.ReflectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by lipeng on 17/1/4.
 */
public class FileWriterResultHandler implements ResultHandler {

    @Override
    public void handle(ResultContext resultContext) throws Exception {
        Object result = resultContext.getResult();
        if (result instanceof PrintableResult && result instanceof FileResult) {
            File file = ((FileResult) result).getFile();
            if (file == null)
                return;
            if (StringUtils.isNotBlank(file.getParent()))
                ReflectionUtils.makeSureDir(new File(file.getParent()));
            IOUtils.write(((PrintableResult) result).getString(), new FileOutputStream(file));
        }
    }
}
