package com.lvbby.codema.java.tool.templateEngine;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.printer.PrettyPrintVisitor;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import com.google.common.collect.Lists;
import com.lvbby.codema.java.entity.JavaAnnotation;
import com.lvbby.codema.java.template.$TemplateUtils_;
import com.lvbby.codema.java.template.annotaion.Foreach;
import com.lvbby.codema.java.template.annotaion.Sentence;
import com.lvbby.codema.java.tool.JavaLexer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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

    /**
     * 处理$TemplateUtils_的函数
     *
     * @param expr
     * @param arg
     */
    @Override
    public void visit(final MethodCallExpr expr, final Void arg) {
        String callerClass = expr.getScope().get().toString();
        String method = expr.getNameAsString();
        String parameter = expr.getArguments().get(0).toString();
        if ($TemplateUtils_.class.getSimpleName().equals(callerClass)) {
            if ("println".equals(method)) {
                printlnTrimString(parameter);
            } else if ("print".equals(method)) {
                printTrimString(parameter);
            } else
                throw new IllegalArgumentException("unknown method in :" + $TemplateUtils_.class.getName());
        } else {
            super.visit(expr, arg);
        }
    }

    private String formatVar(String s) {
        return removeFirstAndEnd(s, "\"", "\"");
    }

    private String removeFirstAndEnd(String src, String prefix, String end) {
        if (src.startsWith(prefix))
            src = src.substring(prefix.length());
        if (src.endsWith(end))
            src = src.substring(0, src.length() - end.length());
        return src;
    }

    @Override
    public void visit(IfStmt n, Void arg) {
        printJavaComment(n.getComment(), arg);//TODO
        Expression condition = n.getCondition();
        if (condition instanceof MethodCallExpr) {
            MethodCallExpr expr = (MethodCallExpr) condition;
            String callerClass = expr.getScope().get().toString();
            String method = expr.getNameAsString();
            String parameter = expr.getArguments().get(0).toString();
            if (parameter.startsWith("\""))
                parameter = parameter.substring(1);
            if (parameter.endsWith("\""))
                parameter = parameter.substring(0, parameter.length() - 1);
            if ($TemplateUtils_.class.getSimpleName().equals(callerClass)) {
                //                printIfStatement(parameter, body, "isTrue".equalsIgnoreCase(method));
                printIfStatement(parameter, n.getThenStmt(), "isTrue".equalsIgnoreCase(method), arg);
                printElse(n.getElseStmt().orElse(null), arg);
            }
        } else {
            //            printIfStatement(condition.toString(), body, true);
            printIfStatement(condition.toString(), n.getThenStmt(), true, arg);

            printElse(n.getElseStmt().orElse(null), arg);
        }

    }

    private void printJavaComment(final Optional<Comment> javacomment, final Void arg) {
        javacomment.ifPresent(c -> c.accept(this, arg));
    }
    private void printIfStatement(String ifString, Statement body, boolean isTrue, Void arg) {
        printTemplateEngineContent(String.format("if(%s%s){", isTrue ? "" : "!", ifString));
        if (body instanceof BlockStmt) {
            for (Statement statement : ((BlockStmt) body).getStatements()) {
                statement.accept(this, arg);
            }
        } else {
            throw new IllegalArgumentException("body in if statement must be block statement");
        }
        printTemplateEngineContent("}");
    }

    private void printElse(Statement elseStatement, Void arg) {
        if (elseStatement == null)
            return;
        if (elseStatement instanceof IfStmt) {
            IfStmt elseIf = (IfStmt) elseStatement;
            printlnTemplateEngineContent("else{");
            visit(elseIf, arg);
            printlnTemplateEngineContent("}");
        } else {
            printlnTemplateEngineContent("else{");
            println(statement2string(elseStatement));
            printlnTemplateEngineContent("}");
        }
    }

    private String statement2string(Statement statement) {
        String s = statement.toString();
        if (s.startsWith("{"))
            s = s.substring(1);
        if (s.endsWith("}"))
            s = s.substring(0, s.length() - 1);
        return s;
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

    protected void printTrimString(String s) {
        printer.print(formatVar(s));
    }

    protected void printlnTrimString(String s) {
        printer.println(formatVar(s));
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
