package com.lvbby.codema.java.testcase;

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
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.java.baisc.JavaSourceParam;
import com.lvbby.codema.java.tool.JavaClassTemplate;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import java.util.EnumSet;
import java.util.List;

import static com.lvbby.codema.core.utils.JavaUtils.binaryReturnOperator;
import static com.lvbby.codema.java.lexer.JavaLexer.*;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaTestcaseCodemaMachine implements CodemaInjectable {
    private static String newVarNameDefault = "result";

    @CodemaRunner
    public void code(CodemaContext codemaContext, JavaTestcaseCodemaConfig config, @NotNull JavaSourceParam source) {
        /** 遍历的模板执行器 */
        JavaClassTemplate.compilationUnitTemplate(codemaContext, config, source, (context, conf, target, src) ->
                config.findResultHandler().handle(codemaContext, config, genTest(target, src)));
    }

    public static CompilationUnit genTest(CompilationUnit parent, ClassOrInterfaceDeclaration typeDeclaration) {
        String beanName = camel(typeDeclaration.getNameAsString());
        parent.getNodesByType(ClassOrInterfaceDeclaration.class).stream().findFirst().ifPresent(testClass -> {
            /** bean field */
            testClass.addField(typeDeclaration.getNameAsString(), beanName, Modifier.PRIVATE).addAnnotation("Autowired");
            /** methods */
            getMethodsFromClassOrInterface(typeDeclaration).forEach(m -> testClass.addMember(genTestMethod(new NameExpr(beanName), m, testClass)));
        });
        return parent;
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
