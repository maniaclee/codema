package com.lvbby.codema.bean;

import com.lvbby.codema.gen.java8.Java8Lexer;
import com.lvbby.codema.gen.java8.Java8Parser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 16/8/3.
 */
public class JavaBeanParser {

    public JavaBean parse(String s) {
        JavaBean re = new JavaBean();
        Lexer lexer = new Java8Lexer(new ANTLRInputStream(s));

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Java8Parser parser = new Java8Parser(tokens);
        // start parsing at the compilationUnit rule
        Java8Parser.CompilationUnitContext t = parser.compilationUnit();
        Java8Parser.TypeDeclarationContext type = t.getChild(Java8Parser.TypeDeclarationContext.class, 0);//second param is the index of dest element
        Java8Parser.ClassDeclarationContext classDeclarationContext = type.classDeclaration();
        Java8Parser.NormalClassDeclarationContext normalClassDeclarationContext = classDeclarationContext.normalClassDeclaration();
        re.setPack(t.packageDeclaration().Identifier().stream().map(ParseTree::getText).collect(Collectors.joining(".")));
        /** class name */
        re.setClassName(normalClassDeclarationContext.Identifier().getText());

        Java8Parser.ClassBodyContext classBodyContext = normalClassDeclarationContext.classBody();
        List<Java8Parser.ClassBodyDeclarationContext> classBodyDeclarationContexts = classBodyContext.classBodyDeclaration();
        for (Java8Parser.ClassBodyDeclarationContext classBodyDeclarationContext : classBodyDeclarationContexts) {
            JavaBeanField javaBeanField = new JavaBeanField();
            Java8Parser.ClassMemberDeclarationContext classMemberDeclarationContext = classBodyDeclarationContext.classMemberDeclaration();
            if (classMemberDeclarationContext != null) {
                Java8Parser.FieldDeclarationContext fieldDeclarationContext = classMemberDeclarationContext.fieldDeclaration();
                if (fieldDeclarationContext == null)
                    continue;
                System.out.println(fieldDeclarationContext.getText());
                javaBeanField.setModifiers(fieldDeclarationContext.fieldModifier().stream().map(f -> f.getText()).collect(Collectors.toList()));
                javaBeanField.setType(fieldDeclarationContext.unannType().getText());
                javaBeanField.setName(findFieldName(fieldDeclarationContext));
                re.getFields().add(javaBeanField);
            }
        }
        return re;
    }

    private String findFieldName(Java8Parser.FieldDeclarationContext f) {
        return f.variableDeclaratorList().variableDeclarator(0).getText();
    }
}
