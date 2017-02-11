package com.lvbby.codema.java.tool;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;

import java.io.FileInputStream;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2017/2/11.
 */
public class JavaCompilationMerger {
    private static final String separator = System.getProperty("line.separator");
    private static final String tab = "    ";
    private final CompilationUnit src;
    private StringBuilder stringBuilder;
    private CompilationUnit dest;
    private ClassOrInterfaceDeclaration classOrInterfaceDeclaration;
    private String result;

    public JavaCompilationMerger(String destSrc, CompilationUnit src) {
        this.stringBuilder = new StringBuilder(destSrc);
        dest = JavaLexer.read(destSrc);
        this.src = src;
        this.classOrInterfaceDeclaration = JavaLexer.getClass(dest).get();
    }

    public JavaCompilationMerger merge() {
        appendWithTab(getLastPosition(), findDiffClass(ClassOrInterfaceDeclaration::getMethods));
        appendWithTab(getFieldPosition(), findDiffClass(ClassOrInterfaceDeclaration::getFields));
        append(getImportPosition(), findDiff(CompilationUnit::getImports).stream().map(s -> s.trim()).collect(Collectors.toList()));
        return this;
    }


    public List<String> findDiffClass(Function<ClassOrInterfaceDeclaration, Collection> function) {
        return findDiff(compilationUnit -> function.apply(JavaLexer.getClass(compilationUnit).get()));
    }

    public List<String> findDiff(Function<CompilationUnit, Collection> function) {
        Collection<Object> diff = CollectionUtils.subtract(function.apply(src), function.apply(dest));
//        Collection<Object> diff = CollectionUtils.subtract(NodeWrapper.from(function.apply(src)), NodeWrapper.from(function.apply(dest)));
        return diff.stream().map(Object::toString).collect(Collectors.toList());
    }


    private int getFieldPosition() {
        return ObjectUtils.firstNonNull(
                _getLine(classOrInterfaceDeclaration.getFields()),
                _getLine(classOrInterfaceDeclaration));
    }

    /***
     * 获取要添加的行位置
     */
    private Integer _getLine(Node node) {
        return node.getBegin().get().line + 1;
    }

    private Integer _getLine(List<? extends Node> nodes) {
        if (CollectionUtils.isEmpty(nodes))
            return null;
        return _getLine(nodes.get(nodes.size() - 1));
    }

    private int getImportPosition() {
        return ObjectUtils.firstNonNull(_getLine(dest.getImports()),
                _getLine(dest.getPackage().get()),
                1);
    }

    private int getLastPosition() {
        return classOrInterfaceDeclaration.getEnd().get().line - 1;
    }


    private void append(int line, List<String> s) {
        if (CollectionUtils.isNotEmpty(s))
            s.forEach(s1 -> append(line, s1));
    }

    private void appendWithTab(int line, List<String> s) {
        append(line, formatTab(s));
    }

    private void append(int line, String s) {
        int i = lineIndex(line);
        Validate.isTrue(i >= 0);
        stringBuilder.insert(i, separator + s);
    }

    private int lineIndex(int line) {
        int index = 0;
        for (int i = 0; i < line; i++) {
            index = stringBuilder.indexOf(separator, index + 1);
            if (index <= 0)
                return -1;
        }
        return index;
    }

    @Override
    public String toString() {
        if (result == null)
            this.result = stringBuilder.toString();
        return result;
    }

    private String formatTab(String s) {
        return tab + s.replaceAll(separator, separator + tab);
    }

    private List<String> formatTab(List<String> s) {
        return s.stream().map(s1 -> formatTab(s1)).collect(Collectors.toList());
    }

    private static class NodeWrapper {
        private Node node;
        private String id;

        public static List<NodeWrapper> from(Collection<?> collection) {
            if (CollectionUtils.isNotEmpty(collection) && collection.iterator().next() instanceof Node) {
                return collection.stream().map(e -> (Node) e).map(e -> new NodeWrapper(e)).collect(Collectors.toList());
            }
            return Lists.newLinkedList();
        }

        public NodeWrapper(Node node) {
            this.node = node;
            this.id = getNodeId(node);
        }

        private String getNodeId(Node node) {
            if (node instanceof MethodDeclaration)
                return ((MethodDeclaration) node).getDeclarationAsString();
            if (node instanceof FieldDeclaration)
                return ((FieldDeclaration) node).getVariable(0).getNameAsString();
            return node.toString();
        }

        @Override
        public String toString() {
            return node.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            NodeWrapper that = (NodeWrapper) o;

            return id != null ? id.equals(that.id) : that.id == null;
        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }
    }

    private void tst(){
        findDiff(CompilationUnit::getImports).stream().map(s -> s.trim()).forEach(System.out::println);
    }

    public static void main(String[] args) throws Exception {
        JavaCompilationMerger javaCompilationMerger = new JavaCompilationMerger(IOUtils.toString(new FileInputStream("/Users/lipeng/workspace/codema/codema-java/src/main/java/com/lvbby/codema/java/entity/JavaArg.java")),
                JavaLexer.read(IOUtils.toString(new FileInputStream("/Users/lipeng/workspace/codema/codema-java/src/main/java/com/lvbby/codema/java/entity/JavaClass.java"))));
        System.out.println(javaCompilationMerger.merge().toString());
//        javaCompilationMerger.tst();

    }
}
