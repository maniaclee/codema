package com.lvbby.codema.java.result;

import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.core.error.CodemaRuntimeException;
import com.lvbby.codema.core.result.AbstractFileResult;
import com.lvbby.codema.core.result.MergeCapableFileResult;
import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaCompilationMerger;
import com.lvbby.codema.java.tool.JavaLexer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.InputStream;

/**
 * Created by lipeng on 2017/1/3.
 */
public class JavaResult extends AbstractFileResult<JavaClass> implements MergeCapableFileResult<JavaClass> {

    private CompilationUnit unit;
    private JavaBasicCodemaConfig config;

    @Override public Result<JavaClass> of(JavaClass javaClass) {
         this.unit=javaClass.getSrc();
         return this;
    }

    public JavaResult config(JavaBasicCodemaConfig config) {
        this.config = config;
        return this;
    }

    @Override
    public File getFile() {
        String destSrcRoot = config.getDestSrcRoot();
        if (StringUtils.isBlank(destSrcRoot))
            throw new CodemaRuntimeException("file dir is empty");
        File file = new File(destSrcRoot);
        if (!file.isDirectory() || !file.exists())
            throw new CodemaRuntimeException("file dir not existed");
        String pack = JavaLexer.getPackage(unit);
        if (StringUtils.isNotBlank(pack)) {
            file = new File(file, pack.replace('.', '/'));
        }
        return new File(file, JavaLexer.getClass(unit).map(clz -> clz.getNameAsString() + ".java").orElseThrow(() -> new CodemaRuntimeException("class not found.")));
    }

    @Override
    public String getString() {
        return unit.toString();
    }


    @Override
    public String parseMergeResult(InputStream dest) throws Exception {
        return new JavaCompilationMerger(IOUtils.toString(dest), unit).merge().toString();
    }
}
