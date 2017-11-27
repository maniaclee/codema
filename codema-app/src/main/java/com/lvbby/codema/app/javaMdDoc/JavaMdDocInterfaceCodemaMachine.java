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
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;

/**
 * Created by dushang.lp on 2017/8/16.
 */
public class JavaMdDocInterfaceCodemaMachine extends AbstractJavaCodemaMachine<JavaMdDocCodemaConfig> {

    private String templateResource="mdJavaDoc.md";
    public void codeEach(CodemaContext codemaContext, JavaMdDocCodemaConfig config,
                         JavaClass cu) throws Exception {

        String template = loadResourceAsString(templateResource);
        JavaMethod method = StringUtils.isBlank(config.getMethod())?null:cu.findMethodByName(config.getMethod());
        if(method!=null) {
            config.handle(codemaContext,
                    new JavaMdTemplateResult(config, template, cu)
                            .bind("javaMethod", method)
                            .bind("method", genClassWithMethod(cu.getSrc(), method.getSrc()))
                            .bind("result", printParam(cu, method.getReturnType()))
                            .bind("parameters", method.getArgs().stream().map(JavaArg::getType)
                                    .filter(javaType -> !javaType.bePrimitive())
                                    .map(javaType -> printParam(cu, javaType))
                                    .filter(s -> s != null)
                                    .collect(Collectors.toList())));
        }else{
            //class
            CompilationUnit clone = cu.getSrc().clone();
            clone.setComment(null);
            clone.setImports(NodeList.nodeList());
            ClassOrInterfaceDeclaration clz = JavaLexer.getClass(clone).get();
            clz.getFields().stream().filter(fieldDeclaration -> fieldDeclaration.isStatic()).forEach(fieldDeclaration -> clz.remove(fieldDeclaration));
            clz.getMethods().stream().filter(m -> m.isStatic()||m.getNameAsString().matches("(set|get).*")).forEach(f->clz.remove(f));
            config.handle(codemaContext,
                    new JavaMdTemplateResult(config, template, cu)
                            .bind("type", clone)
                            .bind("javaMethod", method)
            );
        }
    }

    private String genClassWithMethod(CompilationUnit compilationUnit, MethodDeclaration methodDeclaration) {
        String declarationAsString = methodDeclaration.getDeclarationAsString();
        return String.format("%s\npublic interface %s{\n\t%s;\n}",
                compilationUnit.getPackageDeclaration().get().toString(),
                JavaLexer.getClass(compilationUnit).get().getNameAsString(), declarationAsString);
    }

    private JavaClass printParam(JavaClass javaClass, JavaType javaType) {
        if (javaType == null || javaType.beVoid())
            return null;
        JavaType targetType = javaType.isGenericType()?javaType.getSpecificType():javaType;
        JavaClass target = javaClass.parseSymbolAsClass(targetType.getName());
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
            return JavaClassSourceParser.fromClassSrc(compilationUnit).loadSource().get(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
