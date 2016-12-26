package com.lvbby.codema.java;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.baisc.JavaSourceParam;
import com.lvbby.codema.java.lexer.JavaLexer;
import com.lvbby.codema.java.tool.JavaClassUtils;

/**
 * Created by lipeng on 2016/12/26.
 */
public abstract class AbstractCodeMachine<T extends JavaBasicCodemaConfig> implements CodemaInjectable {


    @CodemaRunner
    public void code(CodemaContext codemaContext, T config, @NotNull JavaSourceParam source) {
        source.getCompilationUnits().forEach(compilationUnit -> {
            CompilationUnit target = JavaClassUtils.createJavaClasss(codemaContext, config, compilationUnit);
            handle(codemaContext, config, target, JavaLexer.getClass(compilationUnit).orElse(null));
        });
    }


    public abstract void handle(CodemaContext codemaContext, T config, CompilationUnit target, ClassOrInterfaceDeclaration src);

}
