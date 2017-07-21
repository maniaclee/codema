package com.lvbby.codema.java.tool.templateEngin;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.printer.PrettyPrintVisitor;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import com.google.common.collect.Lists;
import com.lvbby.codema.java.entity.JavaAnnotation;
import com.lvbby.codema.java.template.annotaion.Foreach;
import com.lvbby.codema.java.template.annotaion.Sentence;
import com.lvbby.codema.java.tool.JavaLexer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by dushang.lp on 2017/7/21.
 */
public class MyPrintVisitory extends PrettyPrintVisitor {

    public MyPrintVisitory(PrettyPrinterConfiguration prettyPrinterConfiguration) {
        super(prettyPrinterConfiguration);
    }

    @Override
    public void visit(FieldDeclaration n, Void arg) {
        List<JavaAnnotation> javaAnnotations = processForeachPre(n);
        processSentence(n);
        super.visit(n, arg);
        processForeachPost(CollectionUtils.isEmpty(javaAnnotations) ? 0 : javaAnnotations.size());
    }

    @Override
    public void visit(MethodDeclaration n, Void arg) {
        List<JavaAnnotation> javaAnnotations = processForeachPre(n);
        processSentence(n);
        super.visit(n, arg);
        processForeachPost(CollectionUtils.isEmpty(javaAnnotations) ? 0 : javaAnnotations.size());
    }

    private void processSentence(NodeWithAnnotations node) {
        handleAnnotation(node, Sentence.class, javaAnnotations -> {
            String s = javaAnnotations.stream().map(javaAnnotation ->
                    javaAnnotation.get(JavaAnnotation.defaultPropertyName).toString())
                    .collect(Collectors.joining("\n"));
            printlnTemplateEngineContent(s);
        });
    }

    private void processForeachPost(int size) {
        if (size > 0) {
            println();
            printTemplateEngineContent(StringUtils.repeat("}", size));
        }
    }

    private <A> void handleAnnotation(NodeWithAnnotations fieldDeclaration, Class<A> clz, Consumer<List<JavaAnnotation>> function) {
        NodeList<AnnotationExpr> annotationByClass = fieldDeclaration.getAnnotations();
        LinkedList<JavaAnnotation> annotations = Lists.newLinkedList();
        for (int i = annotationByClass.size() - 1; i >= 0; i--) {
            if (clz.getSimpleName().equals(annotationByClass.get(i).getNameAsString())) {
                annotations.addFirst(JavaLexer.parseAnnotation(annotationByClass.get(i)));
                annotationByClass.remove(i);
            }
        }
        if (!annotations.isEmpty()) {
            function.accept(annotations);
        }
    }

    private <A, T> T handleAnnotationReturn(NodeWithAnnotations fieldDeclaration, Class<A> clz, Function<List<JavaAnnotation>, T> function) {
        NodeList<AnnotationExpr> annotationByClass = fieldDeclaration.getAnnotations();
        LinkedList<JavaAnnotation> annotations = Lists.newLinkedList();
        for (int i = annotationByClass.size() - 1; i >= 0; i--) {
            if (clz.getSimpleName().equals(annotationByClass.get(i).getNameAsString())) {
                annotations.addFirst(JavaLexer.parseAnnotation(annotationByClass.get(i)));
                annotationByClass.remove(i);
            }
        }
        if (!annotations.isEmpty()) {
            return function.apply(annotations);
        }
        return null;
    }

    private List<JavaAnnotation> processForeachPre(NodeWithAnnotations fieldDeclaration) {
        return handleAnnotationReturn(fieldDeclaration, Foreach.class, annotations -> {
            String result = annotations.stream().map(javaAnnotation ->
                    String.format("for(%s){", javaAnnotation.get(JavaAnnotation.defaultPropertyName).toString())
                            + javaAnnotation.getList("body").stream().map(o -> o.toString() + ";").collect(Collectors.joining("\n"))
            ).collect(Collectors.joining("\n"));
            printlnTemplateEngineContent(result);
            return annotations;
        });
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
