package com.lvbby.codema.java.template;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.lvbby.codema.java.template.entity.JavaArg;
import com.lvbby.codema.java.template.entity.JavaClass;
import com.lvbby.codema.java.template.entity.JavaField;
import com.lvbby.codema.java.template.entity.JavaMethod;
import com.lvbby.codema.java.tool.JavaLexer;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 17/1/5.
 */
public class JavaClassParser {

    public JavaClass parse(CompilationUnit cu) {
        JavaClass clz = new JavaClass();
        ClassOrInterfaceDeclaration c = JavaLexer.getClass(cu).orElse(null);
        if (c == null)
            return null;
        clz.setName(c.getNameAsString());
        clz.setNameCamel(JavaLexer.camel(clz.getName()));
        clz.setFields(parseFields(c));
        clz.setMethods(parseMethods(c));
        return clz;
    }

    private List<JavaMethod> parseMethods(ClassOrInterfaceDeclaration c) {
        return JavaLexer.getMethods(c).stream().map(m -> {
            JavaMethod re = new JavaMethod();
            re.setName(m.getNameAsString());
            re.setReturnType(m.getType().toString());
            if (re.getReturnType().equals("void"))
                re.setReturnType(null);
            re.setArgs(m.getParameters().stream().map(p -> {
                JavaArg arg = new JavaArg();
                arg.setName(p.getNameAsString());
                arg.setType(StringUtils.trimToNull(p.getType().toString()));
                return arg;
            }).collect(Collectors.toList()));
            return re;
        }).collect(Collectors.toList());
    }

    private List<JavaField> parseFields(ClassOrInterfaceDeclaration cu) {
        return cu.getFields().stream().map(f -> {
            JavaField re = new JavaField();
            VariableDeclarator variable = f.getVariable(0);
            re.setName(variable.getNameAsString());
            re.setType(variable.getType().toString());
            return re;
        }).collect(Collectors.toList());
    }


}
