package com.lvbby.codema.lexer;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 16/12/16.
 */
public class JavaLexer {
    public static CompilationUnit read(String code) {
        return JavaParser.parse(code);
    }

    public static TypeDeclaration<?> parseJavaClass(String code) {
        return read(code).getType(0);
    }

    public static List<FieldDeclaration> getFields(CompilationUnit cu) {
        if (CollectionUtils.isEmpty(cu.getTypes()) || cu.getTypes().size() < 1)
            return Lists.newLinkedList();
        return getFields(cu.getType(0));
    }

    public static List<FieldDeclaration> getFields(TypeDeclaration<?> cu) {
        return cu.getFields().stream().filter(f -> isProperty(f)).collect(Collectors.toList());
    }


    public static List<MethodDeclaration> getMethodsFromClassOrInterface(TypeDeclaration<?> cu) {
        ClassOrInterfaceDeclaration classOrInterfaceType = (ClassOrInterfaceDeclaration) cu;
        if (classOrInterfaceType.isInterface()) {
            return getMethods(cu);
        }
        return getMethods(cu, Modifier.PUBLIC);
    }

    public static List<MethodDeclaration> getMethods(TypeDeclaration<?> cu, Modifier... modifiers) {
        List<Modifier> ms = modifiers == null || modifiers.length == 0 ? null : Lists.newArrayList(modifiers);
        return cu.getMethods().stream().filter(m -> ms == null || m.getModifiers().containsAll(ms)).collect(Collectors.toList());
    }

    public static boolean isProperty(FieldDeclaration n) {
        return !n.isStatic() && !n.isTransient();
    }

    public static String getFieldName(FieldDeclaration fieldDeclaration) {
        return fieldDeclaration.getVariable(0).getNameAsString();
    }

    public static String getFieldGetterName(FieldDeclaration fieldDeclaration) {
        return camel("get", getFieldName(fieldDeclaration));
    }

    public static String getFieldSetterName(FieldDeclaration fieldDeclaration) {
        return camel("set", getFieldName(fieldDeclaration));
    }

    public static String camel(String s, String... ss) {
        if (ss == null || ss.length == 0)
            return StringUtils.uncapitalize(s);
        return s.toLowerCase() + Lists.newArrayList(ss).stream().map(e -> StringUtils.capitalize(e)).collect(Collectors.joining());
    }

    public static ClassOrInterfaceType type(String s) {
        return new ClassOrInterfaceType(s);
    }


    public static VariableDeclarationExpr newVar(ClassOrInterfaceType type, String var) {
        return new VariableDeclarationExpr(new VariableDeclarator(type, var, new ObjectCreationExpr(null, type, new NodeList<>())));
    }


}
