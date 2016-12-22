package com.lvbby.codema.coder.java;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.lvbby.codema.lexer.JavaLexer;

/**
 * Created by lipeng on 2016/12/21.
 */
public class JavaSpringUtils {

    public static ClassOrInterfaceDeclaration addAutowiredField(ClassOrInterfaceDeclaration testClass, TypeDeclaration typeDeclaration) {
        testClass.addField(typeDeclaration.getNameAsString(), JavaLexer.camel(typeDeclaration.getNameAsString()), Modifier.PRIVATE).addAnnotation("Autowired");
        return testClass;
    }
}
