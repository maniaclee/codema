package com.lvbby.codema.java.result;

import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.core.error.CodemaRuntimeException;
import com.lvbby.codema.core.result.FileResult;
import com.lvbby.codema.core.result.PrintableResult;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.tool.JavaLexer;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * Created by lipeng on 2017/1/3.
 */
public class JavaResult implements PrintableResult, FileResult {

    private CompilationUnit unit;
    private JavaBasicCodemaConfig config;

    public JavaResult(CompilationUnit unit, JavaBasicCodemaConfig config) {
        this.unit = unit;
        this.config = config;
    }

    @Override
    public Object getResult() {
        return unit;
    }

    @Override
    public File getFile() {
        String destSrcRoot = config.getDestSrcRoot();
        if (StringUtils.isBlank(destSrcRoot))
            throw new CodemaRuntimeException("file dir is empty");
        File file = new File(destSrcRoot);
        if (!file.isDirectory() || !file.exists())
            throw new CodemaRuntimeException("file dir not existed");
        String pack = unit.getPackage().map(e -> e.getPackageName()).orElse("");
        if (StringUtils.isNotBlank(pack)) {
            file = new File(file, pack.replace('.', '/'));
        }
        return new File(file, JavaLexer.getClass(unit).map(clz -> clz.getNameAsString() + ".java").orElseThrow(() -> new CodemaRuntimeException("class not found.")));
    }

    public CompilationUnit getUnit() {
        return unit;
    }

    @Override
    public String getString() {
        return unit.toString();
    }
}
