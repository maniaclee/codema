package com.lvbby.codema.java.tool;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.baisc.JavaSourceParam;
import com.lvbby.codema.java.lexer.JavaLexer;

/**
 * Created by lipeng on 2016/12/26.
 */
public class JavaClassTemplate {

    public interface ClassOrInterfaceTemplate<T extends JavaBasicCodemaConfig> {
        void handle(CodemaContext codemaContext, T config, ClassOrInterfaceDeclaration target, ClassOrInterfaceDeclaration src);
    }

    public interface CompilationUnitTemplate<T extends JavaBasicCodemaConfig> {
        void handle(CodemaContext codemaContext, T config, CompilationUnit target, ClassOrInterfaceDeclaration src);
    }

    public static <T extends JavaBasicCodemaConfig> void classOrInterfaceTemplate(CodemaContext codemaContext, T config, @NotNull JavaSourceParam source, ClassOrInterfaceTemplate classOrInterfaceTemplate) {
        source.getCompilationUnits().forEach(compilationUnit -> {
            CompilationUnit target = JavaClassUtils.createJavaClasss(codemaContext, config, compilationUnit);
            classOrInterfaceTemplate.handle(codemaContext, config, JavaLexer.getClass(target).orElse(null), JavaLexer.getClass(compilationUnit).orElse(null));
        });
    }

    public static <T extends JavaBasicCodemaConfig> void compilationUnitTemplate(CodemaContext codemaContext, T config, @NotNull JavaSourceParam source, CompilationUnitTemplate classOrInterfaceTemplate) {
        source.getCompilationUnits().forEach(compilationUnit -> {
            CompilationUnit target = JavaClassUtils.createJavaClasss(codemaContext, config, compilationUnit);
            classOrInterfaceTemplate.handle(codemaContext, config, target, JavaLexer.getClass(compilationUnit).orElse(null));
        });
    }
}
