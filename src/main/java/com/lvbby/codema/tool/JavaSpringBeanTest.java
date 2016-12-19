package com.lvbby.codema.tool;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.VoidType;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.EnumSet;

import static com.lvbby.codema.lexer.JavaLexer.*;

/**
 * Created by lipeng on 16/12/16.
 */
public class JavaSpringBeanTest {


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
        TypeDeclaration<?> a = genTest(IOUtils.toString(new FileInputStream("/Users/psyco/workspace/github/codema/src/main/java/com/lvbby/codema/render/TemplateEngine.java")));
        System.out.println(a);
    }

    public static MethodDeclaration genTestMethod(NameExpr bean, MethodDeclaration m) {
        MethodDeclaration methodDeclaration = new MethodDeclaration(EnumSet.of(Modifier.PUBLIC), VoidType.VOID_TYPE, m.getNameAsString())
                .setBody(new BlockStmt().addStatement(genTestStatement(bean, m)));
        methodDeclaration.addAnnotation(Test.class);
        return methodDeclaration;
    }

    private static Expression genTestStatement(NameExpr bean, MethodDeclaration m) {

        MethodCallExpr methodCallExpr = new MethodCallExpr(bean, m.getNameAsString())
                .addArgument("");
        return methodCallExpr;
    }

}
