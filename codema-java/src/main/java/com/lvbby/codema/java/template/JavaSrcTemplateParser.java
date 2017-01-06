package com.lvbby.codema.java.template;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.imports.ImportDeclaration;
import com.google.common.collect.Lists;
import com.lvbby.codema.java.tool.JavaLexer;
import com.lvbby.codema.java.tool.JavaSrcLoader;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lipeng on 17/1/5.
 * 将Java源文件转换成模板引擎template
 */
public class JavaSrcTemplateParser {
    private List<JavaTemplateAnnotationHandler> handlers = Lists.newArrayList(new $IfHandler(), new $ForeachHandler(), new $ExprHandler());

    public String parse(Class templateClass) {
        CompilationUnit cu = JavaSrcLoader.getJavaSrcCompilationUnit(templateClass);
        filterImport(cu);
        return eval(cu.toString());
    }

    private String eval(String s) {
        String re = s.replaceAll("//", "").replace("$", "${").replace("__", ".").replace('_', '}');
        re = filterBlockComment(re);
        return re;
    }

    private static String filterBlockComment(String s) {
        StringBuilder re = new StringBuilder();
        int last = 0;
        Matcher matcher = Pattern.compile("/\\*{1,2}#([^*/]+)\\*/").matcher(s);
        while (matcher.find()) {
            re.append(s.substring(last, matcher.start()));
            re.append(matcher.group(1));
            last = matcher.end();
        }
        re.append(s.substring(last));
        return re.toString();
    }


    private Optional<JavaTemplateAnnotationHandler> getAnnotationHandler(Annotation annotation) {
        return handlers.stream().filter(javaTemplateAnnotationHandler -> javaTemplateAnnotationHandler.annotation(annotation)).findFirst();
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

    private void handleMethods(ClassOrInterfaceDeclaration clzCu, Class templateClass) {
        try {
            for (MethodDescriptor methodDescriptor : Introspector.getBeanInfo(templateClass, Object.class).getMethodDescriptors()) {
                List<String> result = Lists.newArrayList();
                Annotation[] annotations = methodDescriptor.getMethod().getAnnotations();
                if (annotations != null) {
                    for (Annotation annotation : annotations) {
                        getAnnotationHandler(annotation).ifPresent(javaTemplateAnnotationHandler -> javaTemplateAnnotationHandler.getString(annotation));
                    }
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        List<MethodDeclaration> methods = JavaLexer.getMethods(clzCu);
        for (MethodDeclaration methodDec : methods) {
            methodDec.setLineComment(null);
        }
    }
}
