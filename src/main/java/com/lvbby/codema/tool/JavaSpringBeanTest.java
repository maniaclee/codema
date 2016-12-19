package com.lvbby.codema.tool;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.VoidType;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.lvbby.codema.lexer.JavaLexer.*;

/**
 * Created by lipeng on 16/12/16.
 */
public class JavaSpringBeanTest {

    private static String newVarNameDefault = "result";

    public static TypeDeclaration<?> genTest(String code) {
        TypeDeclaration<?> typeDeclaration = parseJavaClass(code);
        String beanName = camel(typeDeclaration.getNameAsString());

        /** test class */
        ClassOrInterfaceDeclaration testClass = new ClassOrInterfaceDeclaration(EnumSet.of(Modifier.PUBLIC), false, typeDeclaration.getNameAsString() + "Test");

        /** bean field */
        testClass.addField(typeDeclaration.getNameAsString(), beanName, Modifier.PRIVATE).addAnnotation("AutoWired");

        /** methods */
        getMethodsFromClassOrInterface(typeDeclaration).forEach(m -> testClass.addMember(genTestMethod(new NameExpr(beanName), m)));
        return testClass;
    }

    @Test
    public void test() throws IOException {
        TypeDeclaration<?> a = genTest(IOUtils.toString(new FileInputStream("/Users/psyco/workspace/dp/ssp-search-service/ssp-es-admin-api/src/main/java/com/dianping/ssp/search/es/admin/api/EsAdminService.java")));
        System.out.println(a);
    }

    public static MethodDeclaration genTestMethod(NameExpr bean, MethodDeclaration m) {
        MethodDeclaration methodDeclaration = new MethodDeclaration(EnumSet.of(Modifier.PUBLIC), VoidType.VOID_TYPE, m.getNameAsString())
                .setBody(new BlockStmt()
                        .addStatement(genTestStatement(bean, m))
                        .addStatement(new NameExpr("System.out.println(re)")));
        methodDeclaration.addAnnotation(Test.class);
        return methodDeclaration;
    }

    private static Expression genTestStatement(NameExpr bean, MethodDeclaration m) {
        MethodCallExpr invokeExpr = new MethodCallExpr(bean, m.getNameAsString()).setArguments(NodeList.nodeList(newType(m)));
        String methodReturnType = methodReturnType(m);
        return methodReturnType == null ? invokeExpr : declareNewVar(type(methodReturnType), newVarNameDefault, invokeExpr);
    }

    private static List<Expression> newType(MethodDeclaration m) {
        return m.getParameters().stream().map(p -> newType(p.getType().toString())).collect(Collectors.toList());
    }

    private static Expression newType(String type) {
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
}
