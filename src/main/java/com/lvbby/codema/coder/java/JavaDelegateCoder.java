package com.lvbby.codema.coder.java;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.google.common.collect.Lists;
import com.lvbby.codema.coder.TypedCoderHandler;
import com.lvbby.codema.coder.java.source.JavaSourceCoderRequest;
import com.lvbby.codema.lexer.JavaLexer;

import java.util.List;

import static com.lvbby.codema.lexer.JavaLexer.*;

/**
 * Created by lipeng on 16/12/21.
 */
public class JavaDelegateCoder extends TypedCoderHandler<JavaSourceCoderRequest> {
    @Override
    public void process(JavaSourceCoderRequest request) throws Exception {
        if (request.getTypeDeclaration() == null) return;

        String var = camel(request.getTypeDeclaration().getNameAsString());

        ClassOrInterfaceDeclaration clz = request.findClass();
        if (clz != null) {
            JavaLexer.addField(clz, request.getTypeDeclaration());
            request.getTypeDeclaration().getMethods().stream().forEach(m -> {
                MethodDeclaration method = delegateMethod(var, m);
                method.setParentNode(clz);
                clz.addMember(method);
            });
        }

    }

    private MethodDeclaration delegateMethod(String var, MethodDeclaration m) {
        MethodDeclaration clone = (MethodDeclaration) m.clone();
        clone.setComment(null);
        clone.setBody(delegateMethodBody(var, m).stream().reduce(new BlockStmt(), (blockStmt, expression) -> blockStmt.addStatement(expression), (blockStmt, blockStmt2) -> blockStmt));
        return clone;
    }

    private List<Expression> delegateMethodBody(String var, MethodDeclaration m) {
        MethodCallExpr invokeExpr = new MethodCallExpr(new NameExpr(var), m.getNameAsString()).setArguments(NodeList.nodeList(JavaLexer.getMethodParameterVars(m)));
        String methodReturnType = JavaLexer.methodReturnType(m);
        if (methodReturnType == null)
            return Lists.newArrayList(invokeExpr);
        return Lists.newArrayList(
                declareNewVar(type(methodReturnType), "re", invokeExpr),
                new NameExpr(String.format("return %s", "re"))
        );
    }
}
