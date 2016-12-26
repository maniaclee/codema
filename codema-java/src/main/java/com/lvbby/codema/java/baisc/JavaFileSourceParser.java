package com.lvbby.codema.java.baisc;

import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.core.SourceParser;
import com.lvbby.codema.java.lexer.JavaLexer;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;

/**
 * Created by lipeng on 2016/12/24.
 */
public class JavaFileSourceParser implements SourceParser {
    @Override
    public Object parse(URI from) throws Exception {
        JavaSourceParam re = new JavaSourceParam();
        File file = new File(from.getPath());
        if (file.isFile()) {
            re.add(parse(file));
            return re;
        }

        if (file.isDirectory())
            for (File f : file.listFiles((dir, name) -> name.endsWith(".java")))
                re.add(parse(f));
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
