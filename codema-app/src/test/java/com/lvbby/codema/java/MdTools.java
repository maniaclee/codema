package com.lvbby.codema.java;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaGlobalContext;
import com.lvbby.codema.core.CodemaGlobalContextKey;
import com.lvbby.codema.java.tool.JavaCodeUtils;
import com.lvbby.codema.java.tool.JavaLexer;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * Created by dushang.lp on 2017/8/18.
 */
public class MdTools {

    private static int titleLevel = 4;

    public void mdDesc(String s, String packageFileter) throws Exception {
        CodemaGlobalContext.set(CodemaGlobalContextKey.directoryRoot,
                Lists.newArrayList("/Users/dushang.lp/workspace/zcbsalescenter"));
        String[] split = s.split("#");
        String service = split[0];
        String method = split[1];
        CompilationUnit compilationUnit = JavaCodeUtils.loadJavaSrcFromProject(service);
        ClassOrInterfaceDeclaration clz = JavaLexer
                .getClass(JavaCodeUtils.loadJavaSrcFromProject(service)).get();
        MethodDeclaration methodDeclaration = clz.getMethodsByName(method).get(0);

        printTitle("接口：");
        printBlock(s);
        printTitle("接口定义");
        printMd(printBody(compilationUnit, methodDeclaration));

        printTitle("参数:");
        for (Parameter parameter : methodDeclaration.getParameters()) {
            String paramType = parameter.getType().toString();
            String fullClassName = findFullClassName(compilationUnit, paramType);
            if (StringUtils.isNotBlank(fullClassName) && fullClassName.startsWith(packageFileter)) {
                printBlock(fullClassName);
                printMd(printParam(fullClassName));
            }
        }

        String returnType = findFullClassName(compilationUnit,
                methodDeclaration.getType().toString());
        if (StringUtils.isNotBlank(returnType) && returnType.startsWith(packageFileter)) {
            printTitle("结果:");
            printBlock(returnType);
            printMd(printParam(returnType));
        }
    }

    private String printParam(String className) throws Exception {
        return printParam(JavaCodeUtils.loadJavaSrcFromProject(className));
    }

    private String printParam(CompilationUnit compilationUnit) {
        ClassOrInterfaceDeclaration paramClz = JavaLexer.getClass(compilationUnit).get();
        for (MethodDeclaration methodDeclaration : paramClz.getMethods()) {
            paramClz.remove(methodDeclaration);
        }

        paramClz.getComment().ifPresent(comment -> paramClz.remove(comment));
        compilationUnit.setImports(NodeList.nodeList());
        compilationUnit.getComment().ifPresent(comment -> compilationUnit.remove(comment));

        paramClz.getFields().stream().filter(fieldDeclaration -> fieldDeclaration.isStatic())
                .forEach(fieldDeclaration -> paramClz.remove(fieldDeclaration));
        return compilationUnit.toString();
    }

    private String printBody(CompilationUnit compilationUnit, MethodDeclaration methodDeclaration) {
        String declarationAsString = methodDeclaration.getDeclarationAsString();
        return String.format("%s\npublic class %s{\n\t%s;\n}",
                compilationUnit.getPackageDeclaration().get().toString(),
                JavaLexer.getClass(compilationUnit).get().getNameAsString(), declarationAsString);
    }

    private String findFullClassName(CompilationUnit compilationUnit, String s) {
        return compilationUnit.getImports().stream()
                .map(importDeclaration -> importDeclaration.toString().trim().replaceAll(";", "")
                        .split("\\s+")[1])
                .filter(importDeclaration -> importDeclaration.endsWith(s)).findAny().orElse(null);
    }

    private StringBuilder resultBuffer = new StringBuilder();

    protected void println(String s) {
        System.out.println(s);
        resultBuffer.append(s).append("\n");
    }

    private void printTitle(String s) {
        println(String.format("%s %s", StringUtils.repeat('#', titleLevel), s));
    }

    private void printBlock(String s) {
        println(String.format("`%s`", s));
    }

    private void printMd(String s) {
        println(String.format("```java\n%s\n```", s));
    }

    private void paste(String s ){
        Clipboard sysClb=null;
        sysClb = Toolkit.getDefaultToolkit().getSystemClipboard();
        sysClb.setContents(new StringSelection(s),null);
    }

    @Test public void testMdDesc() throws Exception {
        mdDesc("com.alipay.zcbsalescenter.common.service.facade.risk.UserRiskTestQueryFacade#queryUserRiskTestJobDescription",
                "com.alipay");
        paste(resultBuffer.toString());
    }
}
