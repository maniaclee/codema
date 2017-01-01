package com.lvbby.codema.java.lexer;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.imports.SingleTypeImportDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public static Optional<ClassOrInterfaceDeclaration> getClass(CompilationUnit cu) {
        return Optional.ofNullable(cu).map(CompilationUnit::getTypes).filter(typeDeclarations -> typeDeclarations.size() > 0).map(typeDeclarations -> (ClassOrInterfaceDeclaration) typeDeclarations.get(0));
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


    public static VariableDeclarationExpr declareNewVarConstructor(ClassOrInterfaceType type, String var) {
        return declareNewVar(type, var, new ObjectCreationExpr(null, type, new NodeList<>()));
    }

    public static VariableDeclarationExpr declareNewVar(ClassOrInterfaceType type, String var, Expression expression) {
        return new VariableDeclarationExpr(new VariableDeclarator(type, var, expression));
    }


    public static Expression newVar(ClassOrInterfaceType type, Expression... expressions) {
        return new ObjectCreationExpr(null, type, NodeList.nodeList(expressions));
    }


    public static String methodReturnType(MethodDeclaration m) {
        String s = m.getType().toString();
        return "void".equalsIgnoreCase(s) ? null : s;
    }

    public static List<Expression> getMethodParameterVars(MethodDeclaration m) {
        return m.getParameters().stream().map(p -> new NameExpr(p.getName())).collect(Collectors.toList());
    }

    public static ClassOrInterfaceDeclaration addField(ClassOrInterfaceDeclaration testClass, TypeDeclaration typeDeclaration) {
        testClass.addField(typeDeclaration.getNameAsString(), JavaLexer.camel(typeDeclaration.getNameAsString()), Modifier.PRIVATE);
        return testClass;
    }

    /***
     * gen parameters' instances
     *
     * @param m
     * @return
     */
    public static List<Expression> newInstanceForDefaultValue(MethodDeclaration m) {
        return m.getParameters().stream().map(p -> newInstanceForDefaultValue(p.getType().toString())).collect(Collectors.toList());
    }

    /***
     * gen instance for given type : like String ->"" , int -> 1, List -> new ArrayList()
     *
     * @param type
     * @return
     */
    public static Expression newInstanceForDefaultValue(String type) {
        String lowerCase = type.toLowerCase().replaceAll("<[^>]+>", "");//remove generic type
        List<String> numbers = Lists.newArrayList("int", "Integer", "short", "double", "float", "byte", "long");
        if (numbers.contains(lowerCase)) {
            return new NameExpr("1");
        }
        if ("String".equalsIgnoreCase(lowerCase))
            return new NameExpr("\"\"");
        ArrayList<String> collections = Lists.newArrayList("collection", "list", "iterable");
        if (collections.contains(lowerCase))
            return new NameExpr("new ArrayList()");
        return newVar(type(type));
    }

    public static <T extends NodeWithAnnotations> T addAnnotationWithImport(T t, Class annotation) {
        t.addAnnotation(annotation);
        if (t instanceof Node) {
            CompilationUnit ancestorOfType = getAncestorOfType((Node) t, CompilationUnit.class);
            if (ancestorOfType != null) {
                if (hasImport(ancestorOfType, annotation))
                    ancestorOfType.addImport(annotation);
            }
        }
        return t;
    }

    public static boolean hasImport(CompilationUnit compilationUnit, Class clz) {
        return compilationUnit.getImports().stream()
                .filter(im -> im instanceof SingleTypeImportDeclaration && ((SingleTypeImportDeclaration) im).getType().getNameAsString().equalsIgnoreCase(clz.getName()))
                .findFirst().isPresent();
    }

    public static <N> N getAncestorOfType(Node node, Class<N> classType) {
        Node parent = node.getParentNode().orElse(null);
        while (parent != null) {
            if (classType.isAssignableFrom(parent.getClass())) {
                return classType.cast(parent);
            }
            parent = parent.getParentNode().orElse(null);
        }
        return null;
    }

}
