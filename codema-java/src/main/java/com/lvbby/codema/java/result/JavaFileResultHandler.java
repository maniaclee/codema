package com.lvbby.codema.java.result;

import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.core.ConfigTypedResultHandler;
import com.lvbby.codema.core.ResultContext;
import com.lvbby.codema.core.error.CodemaRuntimeException;
import com.lvbby.codema.java.app.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.tool.JavaLexer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lipeng on 17/1/3.
 */
public class JavaFileResultHandler extends ConfigTypedResultHandler<JavaBasicCodemaConfig> {
    @Override
    protected void process(ResultContext resultContext, JavaBasicCodemaConfig config) {
        if (!(resultContext.getResult() instanceof CompilationUnit))
            return;
        String destSrcRoot = config.getDestSrcRoot();
        if (StringUtils.isBlank(destSrcRoot))
            throw new CodemaRuntimeException("file dir is empty");
        File file = new File(destSrcRoot);
        if (!file.isDirectory() || !file.exists())
            throw new CodemaRuntimeException("file dir not existed");
        CompilationUnit result = (CompilationUnit) resultContext.getResult();
        String pack = result.getPackage().map(e -> e.getPackageName()).orElse("");
        if (StringUtils.isNotBlank(pack)) {
            file = new File(file, pack.replace('.', '/'));
            if (!file.mkdirs())
                throw new CodemaRuntimeException("error mkdir : " + file.getAbsolutePath());
        }
        File javaFile = new File(file, JavaLexer.getClass(result).map(clz -> clz.getNameAsString() + ".java").orElseThrow(() -> new CodemaRuntimeException("class not found.")));
        System.out.println(javaFile.getAbsolutePath());
        try {
            IOUtils.write(result.toString(), new FileOutputStream(javaFile));
        } catch (IOException e) {
            e.printStackTrace();
            throw new CodemaRuntimeException(String.format("error writing to file : %s", javaFile.getAbsolutePath()));
        }
    }
}
