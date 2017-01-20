package com.lvbby.codema.java.source;

import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.core.SourceParser;
import com.lvbby.codema.java.baisc.JavaSourceParam;
import com.lvbby.codema.java.tool.JavaClassUtils;
import com.lvbby.codema.java.tool.JavaLexer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;

/**
 * Created by lipeng on 2016/12/24.
 */
public class JavaFileSourceParser implements SourceParser<JavaSourceParam> {
    @Override
    public JavaSourceParam parse(URI from) throws Exception {
        JavaSourceParam re = new JavaSourceParam();
        File file = new File(from.getPath());
        if (file.isFile()) {
            re.add(JavaClassUtils.convert(parse(file)));
        } else if (file.isDirectory()) {
            for (File f : file.listFiles((dir, name) -> name.endsWith(".java")))
                re.add(JavaClassUtils.convert(parse(f)));
        }
        Validate.notEmpty(re.getClasses(), "not source found");
        return re;
    }


    private CompilationUnit parse(File file) throws Exception {
        return JavaLexer.read(IOUtils.toString(new FileInputStream(file)));
    }

    @Override
    public String getSupportedUriScheme() {
        return "file://java:src/";
    }

}
