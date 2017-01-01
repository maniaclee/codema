package com.lvbby.codema.java.tool;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.baisc.JavaSourceParam;
import com.lvbby.codema.java.lexer.JavaLexer;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2016/12/25.
 */
public class JavaClassUtils {
    public static List<CompilationUnit> createJavaClasses(CodemaContext request, JavaBasicCodemaConfig config, JavaSourceParam javaSourceParam) {
        return Optional.ofNullable(javaSourceParam)
                .map(e -> e.getCompilationUnits())
                .map(compilationUnits -> compilationUnits.stream().map(compilationUnit -> createJavaClasss(request, config, compilationUnit)).collect(Collectors.toList())).orElse(Lists.newLinkedList());
    }

    public static CompilationUnit createJavaClasss(CodemaContext request, JavaBasicCodemaConfig config, CompilationUnit compilationUnit) {
        CompilationUnit re = new CompilationUnit();
        //package
        if (StringUtils.isNotBlank(config.getDestPackage()))
            re.setPackage(config.getDestPackage());
        //class
        ClassOrInterfaceDeclaration clz = createClass(request, config, JavaLexer.getClass(compilationUnit).map(cu -> cu.getNameAsString()).orElse(null));
        clz.setParentNode(re);
        re.setTypes(NodeList.nodeList(clz));
        return re;
    }

    public static CompilationUnit createJavaClasssUnit(String classFullpath, String author, boolean isInterface) {
        int i = classFullpath.lastIndexOf('.');
        String pack = i > 0 ? classFullpath.substring(0, i) : null;
        String className = i > 0 ? classFullpath.substring(i + 1) : classFullpath;
        return createJavaClasssUnit(className, pack, author, isInterface);
    }

    public static CompilationUnit createJavaClasssUnit(String className, String packageName, String author, boolean isInterface) {
        CompilationUnit re = new CompilationUnit();
        //package
        if (StringUtils.isNotBlank(packageName))
            re.setPackage(packageName);
        //class
        re.setTypes(NodeList.nodeList(createJavaClasss(className, author, isInterface)));
        return re;
    }

    public static ClassOrInterfaceDeclaration createJavaClasss(String className, String author, boolean isInterface) {
        //class
        ClassOrInterfaceDeclaration clz = new ClassOrInterfaceDeclaration(EnumSet.of(Modifier.PUBLIC), false, className).setInterface(isInterface);
        if (StringUtils.isNotBlank(author))
            clz.setJavaDocComment(String.format("\n* Created by %s on %s\n", author, new SimpleDateFormat("yyyy/MM/hh").format(new Date())));
        return clz;
    }

    public static ClassOrInterfaceDeclaration createClass(CodemaContext request, JavaBasicCodemaConfig config, String className) {
        if (StringUtils.isBlank(className))
            className = "Untitled" + UUID.randomUUID();
        else
            className += config.getDestClassName();//TODO

        ClassOrInterfaceDeclaration re = new ClassOrInterfaceDeclaration(EnumSet.of(Modifier.PUBLIC), false, className).setInterface(config.isToBeInterface());
        re.setJavaDocComment(String.format("\n* Created by %s on %s\n", config.getAuthor(), new SimpleDateFormat("yyyy/MM/hh").format(new Date())));
        return re;
    }
}
