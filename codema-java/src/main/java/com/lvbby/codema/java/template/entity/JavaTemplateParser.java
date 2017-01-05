package com.lvbby.codema.java.template.entity;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.imports.ImportDeclaration;
import com.google.common.collect.Lists;
import com.lvbby.codema.java.tool.JavaLexer;
import com.lvbby.codema.java.tool.JavaSrcLoader;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lipeng on 17/1/5.
 */
public class JavaTemplateParser {

    public String parse(Class templateClass) {
        CompilationUnit cu = JavaSrcLoader.getJavaSrcCompilationUnit(templateClass);
        filterImport(cu);
        ClassOrInterfaceDeclaration clz = JavaLexer.getClass(cu).orElseThrow(() -> new IllegalArgumentException("class not found : " + templateClass.getName()));
        clz.setJavaDocComment("");
        return cu.toString();
    }

    public static void filterImport(CompilationUnit cu) {
        ArrayList<ImportDeclaration> imports = Lists.newArrayList(cu.getImports());
        for (int i = 0; i < imports.size(); i++) {
            if (!isValidImport(imports.get(i), "com.lvbby.codema.java.template"))
                cu.getImports().remove(imports.get(i));
        }
    }

    private static boolean isValidImport(ImportDeclaration importDeclaration, String pack) {
        Matcher matcher = Pattern.compile("import\\s+(static\\s+)?([^;]+)").matcher(importDeclaration.toString());
        if (matcher.find())
            return !matcher.group(2).startsWith(pack);
        return true;
    }
}
