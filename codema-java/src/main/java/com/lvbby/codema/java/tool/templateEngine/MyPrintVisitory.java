package com.lvbby.codema.java.tool.templateEngine;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.printer.PrettyPrintVisitor;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import com.google.common.collect.Lists;
import com.lvbby.codema.java.entity.JavaAnnotation;
import com.lvbby.codema.java.template.__TemplateUtils_;
import com.lvbby.codema.java.template.annotaion.Foreach;
import com.lvbby.codema.java.template.annotaion.Sentence;
import com.lvbby.codema.java.tool.JavaLexer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
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

    private static final CodemaTemplateUtilsClassHelper utilsClassHelper = new CodemaTemplateUtilsClassHelper(__TemplateUtils_.class);

    public MyPrintVisitory(PrettyPrinterConfiguration prettyPrinterConfiguration) {
        super(prettyPrinterConfiguration);
    }

    @Override
    public void visit(final ClassOrInterfaceDeclaration n, final Void arg) {
        processSentence(n);
        super.visit(n, arg);
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
        if (expr.getScope().isPresent()) {
            String callerClass = expr.getScope().get().toString();
            String method = expr.getNameAsString();
            if (utilsClassHelper.isClassName(callerClass) && expr.getArguments().isNonEmpty()) {
                String parameter = expr.getArguments().get(0).toString();
                if ("println".equals(method)) {
                    printlnTrimString(parameter);
                } else if ("print".equals(method)) {
                    printTrimString(parameter);
                } else
                    throw new IllegalArgumentException(
                            "unknown method in :" + __TemplateUtils_.class.getName());
                return;
            }
        }
        super.visit(expr, arg);
    }

    private static class CodemaTemplateUtilsClassHelper {
        private Class<?> utilsClass;

        public CodemaTemplateUtilsClassHelper(Class<?> utilsClass) {
            this.utilsClass = utilsClass;
        }

        public boolean isClassName(String s) {
            return StringUtils.equals(s, utilsClass.getSimpleName());
        }

        public Method getMethod(String s) {
            Validate.notBlank(s, "method name can't be empty");
            List<Method> collect = Arrays.stream(utilsClass.getMethods())
                    .filter(method -> Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers()))
                    .filter(method -> method.getName().equals(s)).collect(Collectors.toList());
            if (collect.size() != 1) {
                throw new RuntimeException(
                        String.format("utils Class can't have overload methods for name : %s", s));
            }
            return collect.get(0);
        }
    }

    /***
     * 处理foreach
     * @param n
     * @param arg
     */
    public void visit(final ForeachStmt n, final Void arg) {
        printJavaComment(n.getComment(), arg);
        Expression iterable = n.getIterable();
        if (iterable instanceof MethodCallExpr) {
            MethodCallExpr expr = (MethodCallExpr) iterable;
            if (expr.getScope().isPresent()) {

                String callerClass = expr.getScope().get().toString();
                String method = expr.getNameAsString();
                String parameter = expr.getArguments().get(0).toString();
                parameter = trimVar(parameter);
                //            $TemplateUtils_.varList()
                if (utilsClassHelper.isClassName(callerClass)) {
                    if (method.equals("varList")) {
                        VariableDeclarationExpr variable = n.getVariable();
                        String format = String.format("for(%s in %s){", variable.getVariable(0).getName(), parameter);
                        printTemplateEngineContent(format);
                        Statement body = n.getBody();
                        if (body instanceof BlockStmt) {
                            for (Statement statement : ((BlockStmt) body).getStatements()) {
                                statement.accept(this, arg);
                            }
                        }
                        printTemplateEngineContent("}");
                        return;
                    }
                }
            }
        }
        super.visit(n, arg);
    }

    /***
     * 去除字符串两边的""
     * @param s
     * @return
     */
    private String trimString(String s) {
        return removeFirstAndEnd(s, "\"", "\"");
    }

    /***
     * 去除var的包装，如${}
     * @param s
     * @return
     */
    private String trimVar(String s) {
        return removeFirstAndEnd(trimString(s), "${", "}");
    }

    private String removeFirstAndEnd(String src, String prefix, String end) {
        if (src.startsWith(prefix)) {
            src = src.substring(prefix.length());
            if (!src.endsWith(end))
                throw new RuntimeException(String.format("%s should be end with %s", src, end));
            src = src.substring(0, src.length() - end.length());
        }
        return src;
    }

    @Override
    public void visit(IfStmt n, Void arg) {
        Expression condition = n.getCondition();
        if (condition instanceof MethodCallExpr) {
            MethodCallExpr expr = (MethodCallExpr) condition;
            String callerClass = expr.getScope().get().toString();
            String method = expr.getNameAsString();
            String parameter = expr.getArguments().get(0).toString();
            parameter = trimVar(parameter);

            //$TemplateUtils_.isTrue()
            if (__TemplateUtils_.class.getSimpleName().equals(callerClass)) {
                /***
                 * if 语句上面必须为block comment，如果line comment的话只能输出一行！！！！
                 */
                printJavaComment(n.getComment(), arg);//TODO
                printIfStatement(parameter, n.getThenStmt(), "isTrue".equalsIgnoreCase(method),
                        arg);
                printElse(n.getElseStmt().orElse(null), arg);
                return;
            }
        }
        super.visit(n, arg);

    }


    private void printJavaComment(final Optional<Comment> javacomment, final Void arg) {
        javacomment.ifPresent(c -> c.accept(this, arg));
    }

    private void printIfStatement(String ifString, Statement body, boolean isTrue, Void arg) {
        if (body instanceof BlockStmt) {
            printTemplateEngineContent(String.format("if(%s%s){", isTrue ? "" : "!", trimVar(ifString)));
            for (Statement statement : ((BlockStmt) body).getStatements()) {
                statement.accept(this, arg);
            }
            printTemplateEngineContent("}");
        } else {
            //            throw new IllegalArgumentException("body in if statement must be block statement");
            body.accept(this, arg);
        }
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
            removeBlockQuote(elseStatement, arg);
            printlnTemplateEngineContent("}");
        }
    }

    private String removeBlock(Statement statement) {
        return removeFirstAndEnd(statement.toString(), "{", "}");
    }

    private void removeBlockQuote(Statement statement,Void arg) {
        if(statement instanceof BlockStmt){
            for (Statement s : ((BlockStmt) statement).getStatements()) {
                s.accept(this,arg);
            }
        }
    }

    private void processSentence(NodeWithAnnotations node) {
        handleAnnotation(node, Sentence.class, javaAnnotations -> {
            String s = javaAnnotations.stream()
                    .map(javaAnnotation -> javaAnnotation.get(JavaAnnotation.defaultPropertyName)
                            .toString()).collect(Collectors.joining("\n"));
            printlnTemplateEngineContent(s);
        });
    }

    private void processForeachPost(int size) {
        if (size > 0) {
            println();
            printTemplateEngineContent(StringUtils.repeat("}", size));
        }
    }

    private <A> void handleAnnotation(NodeWithAnnotations nodeWithAnnotations, Class<A> clz,
                                      Consumer<List<JavaAnnotation>> function) {
        NodeList<AnnotationExpr> annotationByClass = nodeWithAnnotations.getAnnotations();
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

    private <A, T> T handleAnnotationReturn(NodeWithAnnotations fieldDeclaration, Class<A> clz,
                                            Function<List<JavaAnnotation>, T> function) {
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
            String result = annotations.stream().map(javaAnnotation -> String.format("for(%s){",
                    javaAnnotation.get(JavaAnnotation.defaultPropertyName).toString())
                    + javaAnnotation
                    .getList("body")
                    .stream().map(o ->
                            o.toString() + ";").collect(Collectors.joining("\n")))
                    .collect(Collectors.joining("\n"));
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
        printer.print(trimString(s));
    }

    protected void printlnTrimString(String s) {
        printer.println(trimString(s));
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
