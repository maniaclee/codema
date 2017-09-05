package com.lvbby.codema.app.javaMdDoc;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.java.entity.JavaArg;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaMethod;
import com.lvbby.codema.java.entity.JavaType;
import com.lvbby.codema.java.machine.AbstractJavaCodemaMachine;
import com.lvbby.codema.java.result.JavaMdTemplateResult;
import com.lvbby.codema.java.source.JavaClassSourceParser;
import com.lvbby.codema.java.tool.JavaLexer;

import java.util.stream.Collectors;

/**
 * Created by dushang.lp on 2017/8/16.
 */
public class JavaMdDocCodemaMachine extends AbstractJavaCodemaMachine<JavaMdDocCodemaConfig> {

    public void codeEach(CodemaContext codemaContext, JavaMdDocCodemaConfig config,
                         JavaClass cu) throws Exception {
        JavaMethod method = cu.findMethodByName(config.getMethod());
        config.handle(codemaContext,
                JavaMdTemplateResult.ofResource(config, loadResourceAsString("mdJavaDoc.md"), cu)
                        .bind("method", genClassWithMethod(cu.getSrc(), method.getSrc()))
                        .bind("result", printParam(cu, method.getReturnType()))
                        .bind("parameters", method.getArgs().stream().map(JavaArg::getType)
                                .filter(javaType -> !javaType.bePrimitive())
                                .map(javaType -> printParam(cu, javaType))
                                .filter(s -> s != null)
                                .collect(
                                        Collectors.toList()))
        );

    }

    private String genClassWithMethod(CompilationUnit compilationUnit, MethodDeclaration methodDeclaration) {
        String declarationAsString = methodDeclaration.getDeclarationAsString();
        return String.format("%s\npublic class %s{\n\t%s;\n}",
                compilationUnit.getPackageDeclaration().get().toString(),
                JavaLexer.getClass(compilationUnit).get().getNameAsString(), declarationAsString);
    }

    private JavaClass printParam(JavaClass javaClass, JavaType javaType) {
        if (javaType == null || javaType.beVoid())
            return null;
        JavaClass target = javaClass.parseSymbolAsClass(javaType.getName());
        if(target==null)
            return null;
        return printParam(target.getSrc());
    }

    private JavaClass printParam(CompilationUnit cu) {
       CompilationUnit compilationUnit=cu.clone();
        ClassOrInterfaceDeclaration paramClz = JavaLexer.getClass(compilationUnit).get();
        for (MethodDeclaration methodDeclaration : paramClz.getMethods()) {
            paramClz.remove(methodDeclaration);
        }

        paramClz.getComment().ifPresent(comment -> paramClz.remove(comment));
        compilationUnit.setImports(NodeList.nodeList());
        compilationUnit.getComment().ifPresent(comment -> compilationUnit.remove(comment));

        paramClz.getFields().stream().filter(fieldDeclaration -> fieldDeclaration.isStatic())
                .forEach(fieldDeclaration -> paramClz.remove(fieldDeclaration));
        try {
            return JavaClassSourceParser.fromClassSrc(compilationUnit).loadSource();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
