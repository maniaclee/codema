package com.lvbby.codema.app.javaMdDoc;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.lvbby.codema.core.AbstractTemplateMachine;
import com.lvbby.codema.core.render.TemplateEngineResult;
import com.lvbby.codema.java.baisc.TemplateResource;
import com.lvbby.codema.java.entity.JavaArg;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaMethod;
import com.lvbby.codema.java.entity.JavaType;
import com.lvbby.codema.java.tool.JavaClassUtils;
import com.lvbby.codema.java.tool.JavaLexer;
import org.apache.commons.collections.CollectionUtils;

import java.util.stream.Collectors;

/**
 * 接口markdown文档
 * Created by dushang.lp on 2017/8/16.
 */
@TemplateResource(resource = "mdJavaDoc.md")
public class JavaMdDocMachine extends AbstractTemplateMachine<JavaClass,String> {

    @Override
    protected void doCode() throws Exception {
        JavaClass cu = source;
        JavaMethod method = CollectionUtils.size(cu.getMethods()) == 1?cu.getMethods().get(0):null;
        TemplateEngineResult result = new TemplateEngineResult(getTemplate())
                .bind("source",cu)
                .bind("javaMethod", method);
        if (method != null) {
             result
                    .bind("method", genClassWithMethod(cu.getSrc(), method.getSrc()))
                    .bind("result", printParam(cu, method.getReturnType()))
                    .bind("parameters", method.getArgs().stream().map(JavaArg::getType).filter(javaType -> !javaType.bePrimitive())
                            .map(javaType -> printParam(cu, javaType)).filter(s -> s != null).collect(Collectors.toList()));
             handle(result);
             return ;
        }
        //class
        CompilationUnit clone = cu.getSrc().clone();
        clone.setImports(NodeList.nodeList());
        clone.setComment(null);
        JavaLexer.getClass(clone).ifPresent(clz -> {
            clz.setComment(null);
            clz.getFields().stream().filter(fieldDeclaration -> fieldDeclaration.isStatic())
                    .forEach(fieldDeclaration -> clz.remove(fieldDeclaration));
            clz.getMethods().stream().filter(m -> m.isStatic() || m.getNameAsString().matches("(set|get).*")).forEach(f -> clz.remove(f));
        });
        handle(result.bind("type", clone));
    }

    private String genClassWithMethod(CompilationUnit compilationUnit, MethodDeclaration methodDeclaration) {
        String declarationAsString = methodDeclaration.getDeclarationAsString();
        return String.format("%s\npublic interface %s{\n\t%s;\n}", compilationUnit.getPackageDeclaration().get().toString(),
                JavaLexer.getClass(compilationUnit).get().getNameAsString(), declarationAsString);
    }

    private JavaClass printParam(JavaClass javaClass, JavaType javaType) {
        if (javaType == null || javaType.beVoid())
            return null;
        JavaType targetType = javaType.isGenericType() ? javaType.getSpecificType() : javaType;
        JavaClass target = javaClass.parseSymbolAsClass(targetType.getName());
        if (target == null)
            return null;
        return printParam(target.getSrc());
    }

    private JavaClass printParam(CompilationUnit cu) {
        CompilationUnit compilationUnit = cu.clone();
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
            return JavaClassUtils.fromClassSrc(compilationUnit);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
