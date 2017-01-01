package com.lvbby.codema.java;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.engine.ScriptEngineFactory;
import com.lvbby.codema.java.engine.JavaEngineContext;
import com.lvbby.codema.java.tool.JavaLexer;
import jd.core.loader.Loader;
import jd.core.loader.LoaderException;
import jd.core.preferences.Preferences;
import jd.core.printer.InstructionPrinter;
import jd.core.printer.Printer;
import jd.core.process.DecompilerImpl;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.net.URI;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2016/12/24.
 */
public class SimpleTest {

    @Test
    public void uri() {
        URI uri = URI.create("asdf:///root/leaf?path=sdf&&sub=sdf");
        System.out.println(uri.getAuthority());
        System.out.println(uri.getPath());
        System.out.println(uri.getQuery());
        System.out.println(uri);
    }

    @Test
    public void name() throws Exception {
        JavaEngineContext parameter = new JavaEngineContext();
        parameter.setFromClassName("shitDto");
        //        String eval = ScriptEngineFactory.instance.eval("script://js/{match: /.*DTO/i.test($fromClassName), result: $fromClassName.replace(/DTO/i, 'Entity')}}", parameter);
        String eval = ScriptEngineFactory.instance.eval("script://js/  (function(){return {match: /.*DTO/i.test($fromClassName), result: $fromClassName.replace(/DTO/i, 'Entity')}})()", parameter);
        System.out.println(eval);

    }

    @Test
    public void test() throws Exception {
        new DecompilerImpl().decompile(new Preferences(), new Loader() {
            @Override
            public DataInputStream load(String s) throws LoaderException {
                try {
                    return new DataInputStream(new FileInputStream(s));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public boolean canLoad(String s) {
                return true;
            }
        }, new InstructionPrinter(new Printer() {
            @Override
            public void print(byte b) {

            }

            @Override
            public void print(char c) {
                System.out.print(c);
            }

            @Override
            public void print(int i) {
                System.out.print(i);
            }

            @Override
            public void print(String s) {
                System.out.print(s);

            }

            @Override
            public void printNumeric(String s) {
                System.out.print(s);

            }

            @Override
            public void printString(String s, String s1) {
                System.out.print(s);
            }

            @Override
            public void printKeyword(String s) {
                System.out.print(s);
            }

            @Override
            public void printJavaWord(String s) {
                System.out.print(s);
            }

            @Override
            public void printType(String s, String s1, String s2) {
                System.out.print(s);
            }

            @Override
            public void printTypeDeclaration(String s, String s1) {
                System.out.print(s);
            }

            @Override
            public void printTypeImport(String s, String s1) {
                System.out.print(s + s1);

            }

            @Override
            public void printField(String s, String s1, String s2, String s3) {
                System.out.print(s);

            }

            @Override
            public void printFieldDeclaration(String s, String s1, String s2) {
                System.out.print(s);

            }

            @Override
            public void printStaticField(String s, String s1, String s2, String s3) {
                System.out.print(s);

            }

            @Override
            public void printStaticFieldDeclaration(String s, String s1, String s2) {
                System.out.print(s);

            }

            @Override
            public void printConstructor(String s, String s1, String s2, String s3) {
                System.out.print(s);

            }

            @Override
            public void printConstructorDeclaration(String s, String s1, String s2) {
                System.out.print(s);
            }

            @Override
            public void printStaticConstructorDeclaration(String s, String s1) {
                System.out.print(s);
            }

            @Override
            public void printMethod(String s, String s1, String s2, String s3) {
                System.err.println(Lists.newArrayList(s, s1, s2, s3).stream().collect(Collectors.joining(" ")));
            }

            @Override
            public void printMethodDeclaration(String s, String s1, String s2) {
                System.err.println(Lists.newArrayList(s, s1, s2).stream().collect(Collectors.joining(" ")));
            }

            @Override
            public void printStaticMethod(String s, String s1, String s2, String s3) {
                System.err.println(Lists.newArrayList(s, s1, s2, s3).stream().collect(Collectors.joining(" ")));
            }

            @Override
            public void printStaticMethodDeclaration(String s, String s1, String s2) {
                System.err.println(Lists.newArrayList(s, s1, s2).stream().collect(Collectors.joining(" ")));
            }

            @Override
            public void start(int i, int i1, int i2) {

            }

            @Override
            public void end() {
                System.out.println();
            }

            @Override
            public void indent() {
                System.out.print("\t");
            }

            @Override
            public void desindent() {

            }

            @Override
            public void startOfLine(int i) {
            }

            @Override
            public void endOfLine() {
                System.out.println();
            }

            @Override
            public void extraLine(int i) {

            }

            @Override
            public void startOfComment() {

            }

            @Override
            public void endOfComment() {

            }

            @Override
            public void startOfJavadoc() {

            }

            @Override
            public void endOfJavadoc() {

            }

            @Override
            public void startOfXdoclet() {

            }

            @Override
            public void endOfXdoclet() {

            }

            @Override
            public void startOfError() {

            }

            @Override
            public void endOfError() {

            }

            @Override
            public void startOfImportStatements() {

            }

            @Override
            public void endOfImportStatements() {

            }

            @Override
            public void startOfTypeDeclaration(String s) {

            }

            @Override
            public void endOfTypeDeclaration() {

            }

            @Override
            public void startOfAnnotationName() {

            }

            @Override
            public void endOfAnnotationName() {

            }

            @Override
            public void startOfOptionalPrefix() {

            }

            @Override
            public void endOfOptionalPrefix() {

            }

            @Override
            public void debugStartOfLayoutBlock() {

            }

            @Override
            public void debugEndOfLayoutBlock() {

            }

            @Override
            public void debugStartOfSeparatorLayoutBlock() {

            }

            @Override
            public void debugEndOfSeparatorLayoutBlock(int i, int i1, int i2) {

            }

            @Override
            public void debugStartOfStatementsBlockLayoutBlock() {

            }

            @Override
            public void debugEndOfStatementsBlockLayoutBlock(int i, int i1, int i2) {

            }

            @Override
            public void debugStartOfInstructionBlockLayoutBlock() {

            }

            @Override
            public void debugEndOfInstructionBlockLayoutBlock() {

            }

            @Override
            public void debugStartOfCommentDeprecatedLayoutBlock() {

            }

            @Override
            public void debugEndOfCommentDeprecatedLayoutBlock() {

            }

            @Override
            public void debugMarker(String s) {

            }

            @Override
            public void debugStartOfCaseBlockLayoutBlock() {

            }

            @Override
            public void debugEndOfCaseBlockLayoutBlock() {

            }
        }), "/Users/lipeng/workspace/codema/codema-java/target/classes/com/lvbby/codema/java/inject/JavaClassParameterFactory.class");
    }

    @Test
    public void java() throws Exception {
        System.out.println(getClass().getResource("/"));
        System.out.println(getClass().getResource("/").getFile().toString());
        System.out.println(getJavaSource(JavaLexer.class));
    }

    public String getJavaSource(Class clz) throws IOException {
        String path = "/" + clz.getName().replace('.', '/')+".java";
        System.out.println(path);
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(path);
        return IOUtils.toString(resourceAsStream);
    }
}
