package com.lvbby.codema.java.template;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaCodemaMachine;
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

    public Map getArgs4te(JavaClass src, AbstractJavaCodemaMachine config) {
        HashMap<Object, Object> map = Maps.newHashMap();
        if (src != null) {
            map.put("from", src);
            map.put("srcClassName", src.getName());
            map.put("srcClassNameUncapitalized", JavaLexer.camel(src.getName()));
            if (config.getJavaClassNameParser() != null) {
                map.put("destClassName", config.getJavaClassNameParser().getClassName(src));
            }
        }
        map.put(_getInnerTemplateClassVar($Null_.class), "");
        map.put(_getInnerTemplateClassVar($NullAnnotation_.class), "");
        map.put("javautil",  new JavaTemplateEngineUtils());
        return map;
    }


    private String _getInnerTemplateClassVar(Class clz) {
        return clz.getSimpleName().replaceAll("[$_]", "");
    }

    public CompilationUnit loadSrcTemplateRaw(AbstractJavaCodemaMachine config,
                                              Class<?> javaSrcTemplate) {
        CompilationUnit cu = JavaSrcLoader.getJavaSrcCompilationUnit(javaSrcTemplate);
        filterImport(cu);
        cu.setPackageDeclaration(config.getDestPackage());
        JavaLexer.getClass(cu).ifPresent(classOrInterfaceDeclaration -> {
            classOrInterfaceDeclaration.setJavadocComment(
                    String.format("\n * Created by sdfsd on %s.\n ",
                            new SimpleDateFormat("yyyy/MM/dd").format(new Date())));
        });
        return cu;
    }

    /***
     * 对模板进行预处理
     * @param s
     * @return
     */
    public static String prepareTemplate(String s) {
        //删除行注释符号//
        String re = ReflectionUtils.replace(s,"(\\s+)(//)\\s*",matcher -> matcher.group(1));
        re = re.replaceAll("\\(\\s+", "(");
        re = filterBlockComment(re);
        re = expr(re);
        return re;
    }

    private static String filterBlockComment(String s) {
        return ReflectionUtils.replace(s, "/\\*{1,2}#\\s*([^*/]+)\\s*\\*{1,2}/", matcher -> matcher.group(1));
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
