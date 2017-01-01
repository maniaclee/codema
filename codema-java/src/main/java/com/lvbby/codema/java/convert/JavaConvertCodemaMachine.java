package com.lvbby.codema.java.convert;

import com.alibaba.fastjson.JSON;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.engine.ScriptEngineFactory;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.java.baisc.JavaSourceParam;
import com.lvbby.codema.java.engine.JavaEngineContext;
import com.lvbby.codema.java.engine.JavaEngineResult;
import com.lvbby.codema.java.inject.JavaTemplate;
import com.lvbby.codema.java.inject.JavaTemplateInjectorProcessor;
import com.lvbby.codema.java.inject.Parameter;
import com.lvbby.codema.java.lexer.JavaLexer;
import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;
import java.util.Optional;

import static com.lvbby.codema.java.lexer.JavaLexer.*;

/**
 * Created by lipeng on 16/12/27.
 */
public class JavaConvertCodemaMachine implements CodemaInjectable {
    @CodemaRunner
    @JavaTemplate
    public void code(CodemaContext codemaContext, JavaConvertCodemaConfig config, @NotNull JavaSourceParam source,
                     @Parameter(identifier = JavaTemplateInjectorProcessor.java_source) CompilationUnit compilationUnitSource,
                     @Parameter(identifier = JavaTemplateInjectorProcessor.java_dest) CompilationUnit compilationUnitDest) throws Exception {

        ClassOrInterfaceDeclaration sourceClass = JavaLexer.getClass(compilationUnitSource).orElse(null);
        ClassOrInterfaceDeclaration destCLass = JavaLexer.getClass(compilationUnitDest).orElse(null);
        if (sourceClass == null || destCLass == null) {
            return;
        }
        String convertToClassName = ScriptEngineFactory.instance.eval(config.getConvertToClassName(), new JavaEngineContext().className(sourceClass.getNameAsString()));
        convertToClassName = Optional.ofNullable(convertToClassName).map(e -> JSON.parseObject(e, JavaEngineResult.class)).filter(e -> e.isMatch()).map(e -> e.getResult()).orElse(null);
        if (StringUtils.isBlank(convertToClassName))
            return;
        MethodDeclaration methodDeclaration = genConvertToMethod(sourceClass, convertToClassName);
        destCLass.addMember(methodDeclaration);
        config.handle(codemaContext, destCLass);
    }

    public static MethodDeclaration genConvertToMethod(ClassOrInterfaceDeclaration typeDeclaration, String otherClass) {
        return genConvertMethod(typeDeclaration, typeDeclaration.getNameAsString(), otherClass);
    }

    public static MethodDeclaration genConvertFromMethod(ClassOrInterfaceDeclaration typeDeclaration, String otherClass) {
        return genConvertMethod(typeDeclaration, otherClass, typeDeclaration.getNameAsString());
    }

    private static MethodDeclaration genConvertMethod(ClassOrInterfaceDeclaration typeDeclaration, String fromClass, String otherClass) {
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


