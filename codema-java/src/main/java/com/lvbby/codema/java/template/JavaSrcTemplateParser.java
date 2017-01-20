package com.lvbby.codema.java.template;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.imports.ImportDeclaration;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lvbby.codema.core.CodemaContextHolder;
import com.lvbby.codema.core.error.CodemaRuntimeException;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaLexer;
import com.lvbby.codema.java.tool.JavaSrcLoader;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lipeng on 17/1/5.
 * 将Java源文件转换成模板引擎template
 */
public class JavaSrcTemplateParser {
    public static JavaSrcTemplateParser instance = new JavaSrcTemplateParser();

    public Map getArgs4te(JavaClass src, JavaBasicCodemaConfig config) {
        HashMap<Object, Object> map = Maps.newHashMap();
        if (src != null) {
            map.put("src", src);
            map.put("from", src.getFrom());
            map.put("TemplateClass", src.getName());
            map.put("templateClass", JavaLexer.camel(src.getName()));
        }
        map.put("config", config);
        map.put("Null", "");
        return map;
    }

    public String loadSrcTemplate(Class templateClass) {
        CompilationUnit cu = JavaSrcLoader.getJavaSrcCompilationUnit(templateClass);
        filterImport(cu);
        return prepareTemplate(cu.toString());
    }

    public String loadSrcTemplateByMethod(TemplateContext context, String methodName) {
        MethodDeclaration methodByName = JavaLexer.getMethodByNameSingle(JavaLexer.getClass(loadSrcTemplateRaw(context)).orElseThrow(() -> new CodemaRuntimeException("no class found")), methodName);
        return prepareTemplate(methodName.toString());
    }

    public String loadSrcTemplate(TemplateContext context) {
        return prepareTemplate(loadSrcTemplateRaw(context).toString());
    }

    public CompilationUnit loadSrcTemplateRaw(TemplateContext context) {
        JavaBasicCodemaConfig javaBasicCodemaConfig = context.getJavaBasicCodemaConfig();
        CompilationUnit cu = JavaSrcLoader.getJavaSrcCompilationUnit(context.getTemplateClass());
        filterImport(cu);
        addImport(cu, context);
        cu.setPackage(StringUtils.isNotBlank(context.getPack()) ? context.getPack() : javaBasicCodemaConfig.getDestPackage());
        JavaLexer.getClass(cu).ifPresent(classOrInterfaceDeclaration -> {
            classOrInterfaceDeclaration.setJavaDocComment(String.format("\n * Created by %s on %s.\n ", javaBasicCodemaConfig.getAuthor(), new SimpleDateFormat("yyyy/MM/dd").format(new Date())));
            if (context.getSource() != null) {
                //parent class
                if (StringUtils.isNotBlank(javaBasicCodemaConfig.getParentClass()))
                    classOrInterfaceDeclaration.addExtends(importAndReturnSimpleClassName(cu, javaBasicCodemaConfig.eval(javaBasicCodemaConfig.getParentClass(), context.getSource().getName())));
                //interfaces
                if (CollectionUtils.isNotEmpty(javaBasicCodemaConfig.getImplementInterfaces())) {
                    javaBasicCodemaConfig.getImplementInterfaces().forEach(e -> classOrInterfaceDeclaration.addImplements(importAndReturnSimpleClassName(cu, javaBasicCodemaConfig.eval(e, context.getSource().getName()))));
                }
            }
        });
        return cu;
    }

    private void addImport(CompilationUnit cu, TemplateContext templateContext) {
        JavaClass javaClassSrc = templateContext.getSource();
        //检查javaClass是否在容器里
        if (javaClassSrc != null) {
            if (CodemaContextHolder.getCodemaContext().getCodema().getCodemaBeanFactory().getBean(javaClassSrc.classFullName()) != null)
                cu.addImport(javaClassSrc.classFullName());
            if (CollectionUtils.isNotEmpty(javaClassSrc.getImports()))
                javaClassSrc.getImports().forEach(i -> cu.addImport(i));
        }
        /** 常用的引用 */
        cu.addImport(List.class);
    }

    private String importAndReturnSimpleClassName(CompilationUnit cu, String className) {
        if (className.contains("."))
            cu.addImport(className);
        return ReflectionUtils.getSimpleClassName(className);
    }

    /***
     * 对模板进行预处理
     * @param s
     * @return
     */
    public static String prepareTemplate(String s) {
        String re = s.replaceAll("//\\s*", "");
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
