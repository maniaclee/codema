package com.lvbby.codema.java.tool;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.java.app.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.app.baisc.JavaSourceParam;
import org.apache.commons.lang3.StringUtils;

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
        source.getCompilationUnits().stream().filter(u -> filterPackage(u, config)).forEach(compilationUnit -> {
            CompilationUnit target = JavaClassUtils.createJavaClasss(codemaContext, config, compilationUnit);
            classOrInterfaceTemplate.handle(codemaContext, config, JavaLexer.getClass(target).orElse(null), JavaLexer.getClass(compilationUnit).orElse(null));
        });
    }

    public static <T extends JavaBasicCodemaConfig> void compilationUnitTemplate(CodemaContext codemaContext, T config, @NotNull JavaSourceParam source, CompilationUnitTemplate classOrInterfaceTemplate) {
        source.getCompilationUnits().stream().filter(u -> filterPackage(u, config)).forEach(compilationUnit -> {
            CompilationUnit target = JavaClassUtils.createJavaClasss(codemaContext, config, compilationUnit);
            classOrInterfaceTemplate.handle(codemaContext, config, target, JavaLexer.getClass(compilationUnit).orElse(null));
        });
    }

    /***
     * 过滤package
     */
    private static boolean filterPackage(CompilationUnit compilationUnit, JavaBasicCodemaConfig config) {
        if (StringUtils.isBlank(config.getFromPackage()))
            return true;
        return compilationUnit.getPackage().map(p -> p.getPackageName()).map(p -> p.startsWith(config.getFromPackage())).orElse(false);
    }
}
