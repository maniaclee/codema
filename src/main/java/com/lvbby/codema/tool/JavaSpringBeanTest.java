package com.lvbby.codema.tool;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.VoidType;
import org.junit.Test;

import java.util.EnumSet;
import java.util.List;

import static com.lvbby.codema.lexer.JavaLexer.*;

/**
 * Created by lipeng on 16/12/16.
 */
public class JavaSpringBeanTest {


    public static TypeDeclaration<?> genTest(String code) {
        TypeDeclaration<?> typeDeclaration = parseJavaClass(code);
        List<MethodDeclaration> methods = getMethods(typeDeclaration, Modifier.PUBLIC);


        ClassOrInterfaceDeclaration classOrInterfaceDeclaration = new ClassOrInterfaceDeclaration(EnumSet.of(Modifier.PUBLIC), false, typeDeclaration.getNameAsString() + "Test");
        classOrInterfaceDeclaration.addAnnotation(Test.class);
        return classOrInterfaceDeclaration;
    }


    public static MethodDeclaration genTestMethod(MethodDeclaration m) {
        MethodDeclaration methodDeclaration = new MethodDeclaration(EnumSet.of(Modifier.PUBLIC), VoidType.VOID_TYPE, m.getDeclarationAsString());
        return methodDeclaration;
    }

    public static MethodDeclaration genConvertToMethod(TypeDeclaration<?> typeDeclaration, String otherClass) {
        return genConvertMethod(typeDeclaration, typeDeclaration.getNameAsString(), otherClass);
    }

    public static MethodDeclaration genConvertFromMethod(TypeDeclaration<?> typeDeclaration, String otherClass) {
        return genConvertMethod(typeDeclaration, otherClass, typeDeclaration.getNameAsString());
    }

    private static MethodDeclaration genConvertMethod(TypeDeclaration<?> typeDeclaration, String fromClass, String otherClass) {
        String srcVar = camel(fromClass);
        String destVar = camel(otherClass);
        BlockStmt blockStmt = getFields(typeDeclaration).stream()
                .reduce(
                        new BlockStmt().addStatement(newVar(type(otherClass), destVar)),
                        (blockStmt1, fieldDeclaration) -> blockStmt1.addStatement(convertStatement(destVar, fieldDeclaration, srcVar)),
                        (blockStmt1, blockStmt2) -> blockStmt1)
                .addStatement(new ReturnStmt().setExpression(new NameExpr(destVar)));
        return new MethodDeclaration(EnumSet.of(Modifier.PUBLIC, Modifier.STATIC), type(otherClass), camel("build", otherClass))
                .addParameter(fromClass, srcVar)
                .setBody(blockStmt);
    }

    /**
     * a.set(b.get())
     */
    private static MethodCallExpr convertStatement(String left, FieldDeclaration fieldDeclaration, String right) {
        NameExpr a = new NameExpr(left);
        NameExpr b = new NameExpr(right);
        return new MethodCallExpr(a, getFieldSetterName(fieldDeclaration)).addArgument(new MethodCallExpr(b, getFieldGetterName(fieldDeclaration)));
    }

}
