package com.lvbby.codema.java.source;

import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.core.source.SourceLoader;
import com.lvbby.codema.java.baisc.JavaSourceParam;
import com.lvbby.codema.java.tool.JavaClassUtils;
import com.lvbby.codema.java.tool.JavaLexer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by lipeng on 2016/12/24.
 */
public class JavaFileSourceParser implements SourceLoader<JavaSourceParam> {
    JavaSourceParam javaSourceParam = new JavaSourceParam();

    public JavaFileSourceParser(File file) throws Exception {
        JavaSourceParam re = new JavaSourceParam();
        if (file.isFile()) {
            re.add(JavaClassUtils.convert(parse(file)));
        } else if (file.isDirectory()) {
            for (File f : file.listFiles((dir, name) -> name.endsWith(".java"))) {
                re.add(JavaClassUtils.convert(parse(f)));
            }
        }
        Validate.notEmpty(re.getClasses(), "not source found");
        this.javaSourceParam = re;
    }

    private CompilationUnit parse(File file) throws Exception {
        return JavaLexer.read(IOUtils.toString(new FileInputStream(file)));
    }

    @Override
    public JavaSourceParam loadSource() throws Exception {
        return javaSourceParam;
    }
}
