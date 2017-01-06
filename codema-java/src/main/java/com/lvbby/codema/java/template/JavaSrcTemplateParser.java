package com.lvbby.codema.java.template;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.imports.ImportDeclaration;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lvbby.codema.core.render.TemplateEngine;
import com.lvbby.codema.core.render.TemplateEngineFactory;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaLexer;
import com.lvbby.codema.java.tool.JavaSrcLoader;

import java.util.ArrayList;
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

    public Map getArgs4te(JavaClass src) {
        HashMap<Object, Object> map = Maps.newHashMap();
        map.put("c", src);
        map.put("TemplateClass", src.getName());
        map.put("templateClass", JavaLexer.camel(src.getName()));
        map.put("Null", "");
        return map;
    }

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
