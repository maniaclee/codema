package com.lvbby.codema.java.handler;

import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.core.ResultContext;
import com.lvbby.codema.core.handler.FileWriterResultHandler;
import com.lvbby.codema.core.result.PrintableResult;
import com.lvbby.codema.java.result.JavaResult;
import com.lvbby.codema.java.tool.JavaCompilationMerger;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by lipeng on 2017/2/11.
 */
public class JavaFileMergeWriterResultHandler extends FileWriterResultHandler {

    @Override
    protected String getContent(PrintableResult result, File destFile, ResultContext resultContext) throws Exception {
        if (!(result instanceof JavaResult))
            return super.getContent(result, destFile, resultContext);
        CompilationUnit unit = ((JavaResult) result).getUnit();
        return new JavaCompilationMerger(IOUtils.toString(new FileInputStream(destFile)), unit).merge().toString();
    }
}
