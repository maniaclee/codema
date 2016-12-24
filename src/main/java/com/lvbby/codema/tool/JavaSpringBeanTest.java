package com.lvbby.codema.tool;

import com.alibaba.fastjson.JSON;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.VoidType;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

import static com.lvbby.codema.lexer.JavaLexer.*;
import static com.lvbby.codema.core.utils.JavaUtils.binaryReturnOperator;

/**
 * Created by lipeng on 16/12/16.
 */
public class JavaSpringBeanTest {

    private static String newVarNameDefault = "result";

    public static TypeDeclaration<?> genTest(String code) {
        return genTest(null, code);
    }

    public static TypeDeclaration<?> genTest(CompilationUnit parent, String code) {
        TypeDeclaration<?> typeDeclaration = parseJavaClass(code);
        String beanName = camel(typeDeclaration.getNameAsString());

        /** test class */
        ClassOrInterfaceDeclaration testClass = new ClassOrInterfaceDeclaration(EnumSet.of(Modifier.PUBLIC), false, typeDeclaration.getNameAsString() + "Test");
        testClass.setParentNode(parent);
        /** bean field */
        testClass.addField(typeDeclaration.getNameAsString(), beanName, Modifier.PRIVATE).addAnnotation("Autowired");

        /** methods */
        getMethodsFromClassOrInterface(typeDeclaration).forEach(m -> testClass.addMember(genTestMethod(new NameExpr(beanName), m, testClass)));
        return testClass;
    }

    public static CompilationUnit genTestClass(String code) {
        CompilationUnit compilationUnit = new CompilationUnit();
        return compilationUnit.setTypes(NodeList.nodeList(genTest(compilationUnit, code)))
                .addImport(JSON.class)
                .addImport("org.springframework.beans.factory.annotation.Autowired");
    }

    @Test
    public void test() throws IOException {
        CompilationUnit a = genTestClass(IOUtils.toString(new FileInputStream("/Users/psyco/workspace/dp/ssp-search-service/ssp-es-admin-api/src/main/java/com/dianping/ssp/search/es/admin/api/EsAdminService.java")));
        System.out.println(a);
    }

    public static MethodDeclaration genTestMethod(NameExpr bean, MethodDeclaration m, TypeDeclaration typeDeclaration) {
        MethodDeclaration methodDeclaration = new MethodDeclaration(EnumSet.of(Modifier.PUBLIC), VoidType.VOID_TYPE, m.getNameAsString());
        methodDeclaration.setParentNode(typeDeclaration);
        methodDeclaration.setBody(genTestStatement(bean, m).stream().reduce(new BlockStmt(), (blockStmt, expression) -> blockStmt.addStatement(expression), binaryReturnOperator()));
        addAnnotationWithImport(methodDeclaration, Test.class);
        if (CollectionUtils.isNotEmpty(m.getThrownExceptions()))
            methodDeclaration.setThrownExceptions(NodeList.nodeList(new ClassOrInterfaceType(Exception.class.getSimpleName())));
        return methodDeclaration;
    }

    private static List<Expression> genTestStatement(NameExpr bean, MethodDeclaration m) {
        MethodCallExpr invokeExpr = new MethodCallExpr(bean, m.getNameAsString()).setArguments(NodeList.nodeList(newInstanceForDefaultValue(m)));
        String methodReturnType = methodReturnType(m);
        if (methodReturnType == null)
            return Lists.newArrayList(invokeExpr);
        return Lists.newArrayList(
                declareNewVar(type(methodReturnType), newVarNameDefault, invokeExpr),
                new NameExpr(String.format("assert %s!=null", newVarNameDefault)),
                new NameExpr(String.format("System.out.println(JSON.toJSONString(%s))", newVarNameDefault))
        );
    }

}
