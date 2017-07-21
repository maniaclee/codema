package com.lvbby.codema.java.tool.templateEngin;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.printer.PrettyPrintVisitor;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import com.google.common.collect.Lists;
import com.lvbby.codema.java.entity.JavaAnnotation;
import com.lvbby.codema.java.template.ForeachSub;
import com.lvbby.codema.java.tool.JavaLexer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Created by dushang.lp on 2017/7/21.
 */
public class MyPrintVisitory extends PrettyPrintVisitor {

    public MyPrintVisitory(PrettyPrinterConfiguration prettyPrinterConfiguration) {
        super(prettyPrinterConfiguration);
    }

    @Override
    public void visit(FieldDeclaration fieldDeclaration, Void arg) {
        NodeList<AnnotationExpr> annotationByClass = fieldDeclaration.getAnnotations();
        LinkedList<JavaAnnotation> annotations = Lists.newLinkedList();
        for (int i = annotationByClass.size() - 1; i >= 0; i--) {
            if (ForeachSub.class.getSimpleName().equals(annotationByClass.get(i).getNameAsString())) {
                annotations.addFirst(JavaLexer.parseAnnotation(annotationByClass.get(i)));
                annotationByClass.remove(i);
            }
        }
        if (!annotations.isEmpty()) {
            String result = annotations.stream().map(javaAnnotation ->
                    String.format("for(%s){", javaAnnotation.get(JavaAnnotation.defaultPropertyName).toString())
                            + javaAnnotation.getList("body").stream().map(o -> o.toString() + ";").collect(Collectors.joining("\n"))
            ).collect(Collectors.joining("\n"));
            printlnTemplateEngineContent(result);
        }
        super.visit(fieldDeclaration, arg);
        println();
        printTemplateEngineContent(StringUtils.repeat("}", annotations.size()));
    }

    @Override
    public void visit(MethodDeclaration n, Void arg) {
        super.visit(n, arg);
        printMyCode(n);
    }

    protected void printTemplateEngineContent(String s) {
        print("<%" + s + "%>");
    }

    protected void printlnTemplateEngineContent(String s) {
        println("<%" + s + "%>");
    }

    protected void print(String s) {
        printer.print(s);
    }

    protected void println(String s) {
        printer.println(s);
    }

    protected void println() {
        printer.println();
    }


    protected void printMyCode(Node node) {
        String data = node.getData(CodemaJavaSourcePrinter.dataKey_fieldAppend);
        if (StringUtils.isNotBlank(data)) {
            print(data);
        }
    }
}
