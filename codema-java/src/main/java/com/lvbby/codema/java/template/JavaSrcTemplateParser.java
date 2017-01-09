package com.lvbby.codema.java.template;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.imports.ImportDeclaration;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lvbby.codema.core.render.TemplateEngine;
import com.lvbby.codema.core.render.TemplateEngineFactory;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.app.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaLexer;
import com.lvbby.codema.java.tool.JavaSrcLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lipeng on 17/1/5.
 * 将Java源文件转换成模板引擎template
 */
public class JavaSrcTemplateParser {
    public static JavaSrcTemplateParser instance = new JavaSrcTemplateParser();

    public TemplateEngine loadJavaSrcTemplateEngine(CompilationUnit cu, Class<?> javaSrcTemplate) {
        String template = parse(javaSrcTemplate);
        JavaClass src = new JavaClassParser().parse(cu);
        TemplateEngine templateEngine = TemplateEngineFactory.create(template);
        return templateEngine
                .bind("c", src)
                .bind("TemplateClass", src.getName())
                .bind("templateClass", JavaLexer.camel(src.getName()))
                .bind("Null", "");
    }

    public Map getArgs4te(CompilationUnit cu) {
        JavaClass src = new JavaClassParser().parse(cu);
        HashMap<Object, Object> map = Maps.newHashMap();
        map.put("c", src);
        map.put("TemplateClass", src.getName());
        map.put("templateClass", JavaLexer.camel(src.getName()));
        map.put("Null", "");
        return map;
    }

    public Map getArgs4te(JavaClass src, JavaBasicCodemaConfig config) {
        HashMap<Object, Object> map = Maps.newHashMap();
        map.put("src", src);
        map.put("from", src.getFrom());
        map.put("TemplateClass", src.getName());
        map.put("templateClass", JavaLexer.camel(src.getName()));
        map.put("config", config);
        map.put("Null", "");
        return map;
    }

    public String parse(Class templateClass) {
        CompilationUnit cu = JavaSrcLoader.getJavaSrcCompilationUnit(templateClass);
        filterImport(cu);
        return render(cu.toString());
    }

    public String parse(Class templateClass, JavaBasicCodemaConfig javaBasicCodemaConfig) {
        CompilationUnit cu = JavaSrcLoader.getJavaSrcCompilationUnit(templateClass);
        filterImport(cu);
        cu.setPackage(javaBasicCodemaConfig.getDestPackage());
        JavaLexer.getClass(cu).ifPresent(classOrInterfaceDeclaration -> {
            classOrInterfaceDeclaration.setJavaDocComment(String.format("\n * Created by %s on %s.\n ", javaBasicCodemaConfig.getAuthor(), new SimpleDateFormat("yyyy/MM/dd").format(new Date())));
        });
        return render(cu.toString());
    }

    private String render(String s) {
        String re = s.replaceAll("//", "");
        re = filterBlockComment(re);
        re = expr(re);
        return re;
    }

    private static String filterBlockComment(String s) {
        return ReflectionUtils.replace(s, "/\\*{1,2}#([^*/]+)\\*/", matcher -> matcher.group(1));
    }

    /**
     * 处理变量表达式
     * ${expr}不处理
     * $abc_   --> ${abc}
     * $abc__xyz_ ---> ${abc.xyz}
     * $abc__xyz_ffff ---> ${abc.xyz}ffff
     */
    private static String expr(String s) {
        return ReflectionUtils.replace(s, "\\$([a-zA-Z0-9]+__)*[a-zA-Z0-9]+_", matcher -> matcher.group().replace("$", "${").replace("__", ".").replace('_', '}'));
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
