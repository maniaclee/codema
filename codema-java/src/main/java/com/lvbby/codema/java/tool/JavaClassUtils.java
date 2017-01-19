package com.lvbby.codema.java.tool;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.error.CodemaRuntimeException;
import com.lvbby.codema.java.app.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.entity.*;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2016/12/25.
 */
public class JavaClassUtils {
    //    public static List<CompilationUnit> createJavaClasses(CodemaContext request, JavaBasicCodemaConfig config, JavaSourceParam javaSourceParam) {
    //        return Optional.ofNullable(javaSourceParam)
    //                .map(e -> e.getClasses())
    //                .map(compilationUnits -> compilationUnits.stream().map(compilationUnit -> createJavaClasss(request, config, compilationUnit)).collect(Collectors.toList())).orElse(Lists.newLinkedList());
    //    }

    //    public static CompilationUnit createJavaClasss(CodemaContext request, JavaBasicCodemaConfig config, CompilationUnit compilationUnit) {
    //        CompilationUnit re = new CompilationUnit();
    //        //package
    //        if (StringUtils.isNotBlank(config.getDestPackage()))
    //            re.setPackage(config.getDestPackage());
    //        //class
    //        ClassOrInterfaceDeclaration clz = createClass(request, config, JavaLexer.getClass(compilationUnit).map(cu -> cu.getNameAsString()).orElse(null));
    //        clz.setParentNode(re);
    //        re.setTypes(NodeList.nodeList(clz));
    //        return re;
    //    }

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

    public static JavaClass convert(CompilationUnit cu) {
        JavaClass re = new JavaClass();
        re.setPack(cu.getPackage().map(packageDeclaration -> packageDeclaration.getNameAsString()).orElse(""));
        ClassOrInterfaceDeclaration clz = JavaLexer.getClass(cu).orElseThrow(() -> new CodemaRuntimeException("no class found"));
        re.setName(clz.getNameAsString());
        re.setImports(JavaLexer.getImports(cu));
        re.setFields(JavaLexer.getFields(cu).stream().map(fieldDeclaration -> {
            VariableDeclarator variable = fieldDeclaration.getVariable(0);
            JavaField javaField = new JavaField();
            javaField.setName(variable.getNameAsString());
            javaField.setType(JavaType.ofClassName(variable.getType().toString()));
            javaField.setPrimitive(false);//TODO
            return javaField;
        }).collect(Collectors.toList()));
        re.setMethods(JavaLexer.getMethods(clz).stream().map(methodDeclaration -> {
            JavaMethod javaMethod = new JavaMethod();
            javaMethod.setName(methodDeclaration.getNameAsString());
            javaMethod.setReturnType(JavaType.ofClassName(methodDeclaration.getType().toString()));
            javaMethod.setArgs(methodDeclaration.getParameters().stream().map(parameter -> {
                JavaArg javaArg = new JavaArg();
                javaArg.setName(parameter.getNameAsString());
                javaArg.setType(JavaType.ofClassName(parameter.getType().toString()));
                return javaArg;
            }).collect(Collectors.toList()));
            return javaMethod;
        }).collect(Collectors.toList()));
        re.setFrom(cu);
        return re;
    }
}
