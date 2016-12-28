package com.lvbby.codema.java.convert;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.java.baisc.JavaSourceParam;
import com.lvbby.codema.java.tool.JavaClassTemplate;

import java.util.EnumSet;

import static com.lvbby.codema.java.lexer.JavaLexer.*;

/**
 * Created by lipeng on 16/12/27.
 */
public class JavaConvertCodemaMachine implements CodemaInjectable {
    @CodemaRunner
    public void code(CodemaContext codemaContext, JavaConvertCodemaConfig config, @NotNull JavaSourceParam source) {
        /** 遍历的模板执行器 */
        JavaClassTemplate.compilationUnitTemplate(codemaContext, config, source, null);
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
                        new BlockStmt().addStatement(declareNewVarConstructor(type(otherClass), destVar)),
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

