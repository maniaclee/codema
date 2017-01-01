package com.lvbby.codema.java.app.convert;

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
import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.engine.ScriptEngineFactory;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.core.inject.Parameter;
import com.lvbby.codema.java.app.baisc.JavaSourceParam;
import com.lvbby.codema.java.engine.JavaEngineContext;
import com.lvbby.codema.java.engine.JavaEngineResult;
import com.lvbby.codema.java.inject.JavaClassParameterFactory;
import com.lvbby.codema.java.inject.JavaTemplate;
import com.lvbby.codema.java.inject.JavaTemplateInjector;
import com.lvbby.codema.java.inject.JavaTemplateParameter;
import com.lvbby.codema.java.template.$GenericTypeArg2_;
import com.lvbby.codema.java.template.$GenericTypeArg_;
import com.lvbby.codema.java.template.JavaTemplateEngine;
import com.lvbby.codema.java.tool.JavaLexer;
import com.lvbby.codema.java.tool.JavaSrcLoader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static com.lvbby.codema.java.tool.JavaLexer.*;

/**
 * Created by lipeng on 16/12/27.
 */
public class JavaConvertCodemaMachine implements CodemaInjectable {
    @CodemaRunner
    @JavaTemplate
    public void code(CodemaContext codemaContext, JavaConvertCodemaConfig config, @NotNull JavaSourceParam source,
                     @JavaTemplateParameter(identifier = JavaTemplateInjector.java_source) CompilationUnit compilationUnitSource,
                     @Parameter(value = "com.lvbby.utils.BuildUtils", createFactory = JavaClassParameterFactory.class) CompilationUnit compilationUnitDest) throws Exception {

        ClassOrInterfaceDeclaration sourceClass = JavaLexer.getClass(compilationUnitSource).orElse(null);
        ClassOrInterfaceDeclaration destCLass = JavaLexer.getClass(compilationUnitDest).orElse(null);
        if (sourceClass == null || destCLass == null) {
            return;
        }
        String convertToClassName = ScriptEngineFactory.instance.eval(config.getConvertToClassName(), new JavaEngineContext().className(sourceClass.getNameAsString()));
        convertToClassName = Optional.ofNullable(convertToClassName).map(e -> JSON.parseObject(e, JavaEngineResult.class)).filter(e -> e.isMatch()).map(e -> e.getResult()).orElse(null);
        if (StringUtils.isBlank(convertToClassName))
            return;
        destCLass.addMember(genConvertToMethod(sourceClass, convertToClassName));
        destCLass.addMember(genConvertFromMethod(sourceClass, convertToClassName));
        destCLass.addMember(genConvertToMethodBatch(sourceClass, sourceClass.getNameAsString(), convertToClassName));
        config.handle(codemaContext, destCLass);
    }

    public static MethodDeclaration genConvertToMethod(ClassOrInterfaceDeclaration typeDeclaration, String otherClass) {
        return genConvertMethod(typeDeclaration, typeDeclaration.getNameAsString(), otherClass);
    }

    public static MethodDeclaration genConvertToMethodBatch(ClassOrInterfaceDeclaration typeDeclaration, String fromClass, String otherClass) {
        //        String srcVar = camel(fromClass, "list");
        //        String destVar = camel(otherClass, "list");
        //        BlockStmt blockStmt = getFields(typeDeclaration).stream()
        //                .reduce(
        //                        new BlockStmt().addStatement(declareNewVarConstructor(type(otherClass), destVar)),
        //                        (blockStmt1, fieldDeclaration) -> blockStmt1.addStatement(convertStatement(destVar, fieldDeclaration, srcVar)),
        //                        (blockStmt1, blockStmt2) -> blockStmt1)
        //                .addStatement(new ReturnStmt().setExpression(new NameExpr(destVar)));
        //        return new MethodDeclaration(EnumSet.of(Modifier.PUBLIC, Modifier.STATIC), listType(otherClass), camel("build", otherClass, "batch"))
        //                .addParameter(list(fromClass), srcVar)
        //                .setBody(blockStmt);

        String methodSrc = JavaSrcLoader.getMethod(JavaConvertCodemaMachine.class, "build$className_Batch").toString();
        String className = new JavaTemplateEngine(methodSrc)
                .bind($GenericTypeArg2_.class, fromClass)
                .bind($GenericTypeArg_.class, otherClass)
                .bind("className", otherClass).render();
        return new MethodDeclaration().setBody(new BlockStmt());
    }


    public String render(String methodDeclaration) {
        return null;
    }

    private static List<$GenericTypeArg_> build$className_Batch(List<$GenericTypeArg2_> src) {
        List<$GenericTypeArg_> re = Lists.newArrayList();
        for ($GenericTypeArg2_ arg : src) {
            //re.add(build$className_Batch(arg));
        }
        return re;
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

    public static void main(String[] args) throws IOException {
        System.out.println(JavaConvertCodemaMachine.class.getResource("/").getFile().toString());
        System.out.println(getJavaSource(JavaConvertCodemaMachine.class));
    }

    public static String getJavaSource(Class clz) throws IOException {
        String path = "/" + clz.getName().replace('.', '/') + ".class";
        System.out.println(path);
        InputStream resourceAsStream = JavaConvertCodemaMachine.class.getClassLoader().getResourceAsStream(path);
        return IOUtils.toString(resourceAsStream);
    }
}


