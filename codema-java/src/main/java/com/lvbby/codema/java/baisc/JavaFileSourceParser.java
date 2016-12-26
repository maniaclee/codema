package com.lvbby.codema.java.baisc;

import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.TypedCodeMachine;
import com.lvbby.codema.core.common.SourceParser;
import com.lvbby.codema.core.config.CoderCommonConfig;
import com.lvbby.codema.java.lexer.JavaLexer;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;

/**
 * Created by lipeng on 2016/12/24.
 */
public class JavaFileSourceParser extends TypedCodeMachine<CodemaJavaBasicConfig> implements SourceParser {
    @Override
    public void code(CodemaContext codemaContext, CodemaJavaBasicConfig codemaJavaBasicConfig) throws Exception {
        CoderCommonConfig config = codemaContext.getConfig(CoderCommonConfig.class);

        JavaSourceParam re = new JavaSourceParam();
        codemaContext.storeParam(re);//store the result

        File file = new File(URI.create(config.getFrom()).getPath());
        if (file.isFile()) {
            re.add(parse(file));
            return;
        }

        if (file.isDirectory())
            for (File f : file.listFiles((dir, name) -> name.endsWith(".java")))
                re.add(parse(f));
    }


    private CompilationUnit parse(File file) throws Exception {
        return JavaLexer.read(IOUtils.toString(new FileInputStream(file)));
    }

    @Override
    public String getSupportedUriScheme() {
        return "file://java:src/";
    }
}
