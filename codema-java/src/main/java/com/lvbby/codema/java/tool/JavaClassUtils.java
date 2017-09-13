package com.lvbby.codema.java.tool;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.google.common.collect.Lists;
import com.lvbby.codema.core.error.CodemaRuntimeException;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.java.entity.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2016/12/25.
 */
public class JavaClassUtils {

    public static CompilationUnit createJavaClassUnit(String classFullpath, String author, boolean isInterface) {
        int i = classFullpath.lastIndexOf('.');
        String pack = i > 0 ? classFullpath.substring(0, i) : null;
        String className = i > 0 ? classFullpath.substring(i + 1) : classFullpath;
        return createJavaClassUnit(className, pack, author, isInterface);
    }

    public static CompilationUnit createJavaClassUnit(String className, String packageName, String author, boolean isInterface) {
        CompilationUnit re = new CompilationUnit();
        //package
        if (StringUtils.isNotBlank(packageName))
            re.setPackageDeclaration(packageName);
        //class
        re.setTypes(NodeList.nodeList(createJavaClass(className, author, isInterface)));
        return re;
    }

    public static ClassOrInterfaceDeclaration createJavaClass(String className, String author, boolean isInterface) {
        //class
        ClassOrInterfaceDeclaration clz = new ClassOrInterfaceDeclaration(EnumSet.of(Modifier.PUBLIC), false, className).setInterface(isInterface);
        if (StringUtils.isNotBlank(author))
            clz.setJavadocComment(String.format("\n* Created by %s on %s\n", author, new SimpleDateFormat("yyyy/MM/hh").format(new Date())));
        return clz;
    }



    public static JavaClass convert(CompilationUnit cu) {
        JavaClass re = new JavaClass();
        re.setPack(JavaLexer.getPackage(cu));
        ClassOrInterfaceDeclaration clz = JavaLexer.getClass(cu).orElseThrow(() -> new CodemaRuntimeException("no class found"));
        re.setName(clz.getNameAsString());
        re.setImports(JavaLexer.getImports(cu));
        re.setFields(JavaLexer.getFields(cu).stream().map(fieldDeclaration -> {
            VariableDeclarator variable = fieldDeclaration.getVariable(0);
            JavaField javaField = new JavaField();
            javaField.setName(variable.getNameAsString());
            javaField.setType(JavaType.ofClassName(variable.getType().toString()));
            javaField.setPrimitive(false);//TODO
            javaField.setAnnotations(fieldDeclaration.getAnnotations().stream().map(annotationExpr -> JavaType.ofClassName(annotationExpr.getNameAsString())).collect(Collectors.toList()));
            javaField.setProperty(hasGetterSetter(clz, javaField.getName()));
            return javaField;
        }).collect(Collectors.toList()));
        re.setMethods(JavaLexer.getMethods(clz).stream().map(methodDeclaration -> {
            JavaMethod javaMethod = new JavaMethod();
            javaMethod.setSrc(methodDeclaration);
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
        re.setSrc(cu);
        return re;
    }

    public static boolean hasGetterSetter(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, String field) {
        return CollectionUtils.isNotEmpty(classOrInterfaceDeclaration.getMethodsByName(JavaCodeUtils.genGetter(field)))
                && CollectionUtils.isNotEmpty(classOrInterfaceDeclaration.getMethodsByName(JavaCodeUtils.genSetter(field)));
    }

    public static List<JavaClass> convert(List<SqlTable> tables) {
        if (tables == null)
            return Lists.newArrayList();
        return tables.stream().map(sqlTable -> convert(sqlTable)).collect(Collectors.toList());
    }

    public static JavaClass convert(SqlTable sqlTable) {
        JavaClass javaClass = new JavaClass();
        javaClass.setPack("");
        javaClass.setName(sqlTable.getName());
        javaClass.setFields(sqlTable.getFields().stream().map(sqlColumn -> {
            JavaField javaField = new JavaField();
            javaField.setName(sqlColumn.getNameCamel());
            javaField.setType(JavaType.ofClass(sqlColumn.getJavaType()));
            javaField.setProperty(true);
            return javaField;
        }).collect(Collectors.toList()));
        javaClass.setFrom(sqlTable);
        return javaClass;
    }
}
