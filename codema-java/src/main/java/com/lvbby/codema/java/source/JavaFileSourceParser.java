package com.lvbby.codema.java.source;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.lvbby.codema.core.SourceParser;
import com.lvbby.codema.core.error.CodemaRuntimeException;
import com.lvbby.codema.java.app.baisc.JavaSourceParam;
import com.lvbby.codema.java.entity.JavaArg;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaField;
import com.lvbby.codema.java.entity.JavaMethod;
import com.lvbby.codema.java.tool.JavaLexer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2016/12/24.
 */
public class JavaFileSourceParser implements SourceParser<JavaSourceParam> {
    @Override
    public JavaSourceParam parse(URI from) throws Exception {
        JavaSourceParam re = new JavaSourceParam();
        File file = new File(from.getPath());
        if (file.isFile()) {
            re.add(convert(parse(file)));
        } else if (file.isDirectory()) {
            for (File f : file.listFiles((dir, name) -> name.endsWith(".java")))
                re.add(convert(parse(f)));
        }
        Validate.notEmpty(re.getClasses(), "not source found");
        return re;
    }

    private JavaClass convert(CompilationUnit cu) {
        JavaClass re = new JavaClass();
        re.setPack(cu.getPackage().map(packageDeclaration -> packageDeclaration.getNameAsString()).orElse(""));
        ClassOrInterfaceDeclaration clz = JavaLexer.getClass(cu).orElseThrow(() -> new CodemaRuntimeException("no class found"));
        re.setName(clz.getNameAsString());
        re.setFields(JavaLexer.getFields(cu).stream().map(fieldDeclaration -> {
            VariableDeclarator variable = fieldDeclaration.getVariable(0);
            JavaField javaField = new JavaField();
            javaField.setName(variable.getNameAsString());
            javaField.setType(variable.getType().toString());
            javaField.setPrimitive(false);//TODO
            return javaField;
        }).collect(Collectors.toList()));
        re.setMethods(JavaLexer.getMethods(clz).stream().map(methodDeclaration -> {
            JavaMethod javaMethod = new JavaMethod();
            javaMethod.setName(methodDeclaration.getNameAsString());
            javaMethod.setReturnType(methodDeclaration.getType().toString());
            javaMethod.setArgs(methodDeclaration.getParameters().stream().map(parameter -> {
                JavaArg javaArg = new JavaArg();
                javaArg.setName(parameter.getNameAsString());
                javaArg.setType(parameter.getType().toString());
                return javaArg;
            }).collect(Collectors.toList()));
            return javaMethod;
        }).collect(Collectors.toList()));
        re.setFrom(cu);
        return re;
    }


    private CompilationUnit parse(File file) throws Exception {
        return JavaLexer.read(IOUtils.toString(new FileInputStream(file)));
    }

    @Override
    public String getSupportedUriScheme() {
        return "file://java:src/";
    }

}
