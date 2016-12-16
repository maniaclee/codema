package com.lvbby.codema.lexer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
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

    public static List<FieldDeclaration> getFields(CompilationUnit cu) {
        if (CollectionUtils.isEmpty(cu.getTypes()) || cu.getTypes().size() < 1)
            return Lists.newLinkedList();
        return getFields(cu.getType(0));
    }

    public static List<FieldDeclaration> getFields(TypeDeclaration<?> cu) {
        return cu.getFields().stream().filter(f -> isProperty(f)).collect(Collectors.toList());
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
            return s.toLowerCase();
        return s.toLowerCase() + Lists.newArrayList(ss).stream().map(e -> StringUtils.capitalize(e)).collect(Collectors.joining());
    }

    public static ClassOrInterfaceType type(String s) {
        return new ClassOrInterfaceType(s);
    }


    public static VariableDeclarationExpr newVar(ClassOrInterfaceType type, String var) {
        return new VariableDeclarationExpr(new VariableDeclarator(type, var, new ObjectCreationExpr(null, type, new NodeList<>())));
    }


}
