package com.lvbby.codema;/*
 [The "BSD license"]
  Copyright (c) 2013 Terence Parr
  All rights reserved.

  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions
  are met:

  1. Redistributions of source code must retain the above copyright
     notice, this list of conditions and the following disclaimer.
  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in the
     documentation and/or other materials provided with the distribution.
  3. The name of the author may not be used to endorse or promote products
     derived from this software without specific prior written permission.

  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
  IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
  IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
  THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.alibaba.fastjson.JSON;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.collect.Lists;
import com.lvbby.codema.bean.JavaBean;
import com.lvbby.codema.bean.JavaBeanDecoder;
import com.lvbby.codema.parser.java8.Java8Lexer;
import com.lvbby.codema.parser.java8.Java8Parser;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.javaparser.ast.type.VoidType.VOID_TYPE;

/* This more or less duplicates the functionality of grun (TestRig) but it
 * has a few specific options for benchmarking like -x2 and -threaded.
 * It also allows directory names as commandline arguments. The simplest test is
 * for the current directory:

~/antlr/code/grammars-v4/java $ java Test .
/Users/parrt/antlr/code/grammars-v4/java8/JavaBaseListener.java
/Users/parrt/antlr/code/grammars-v4/java8/Java8Lexer.java
/Users/parrt/antlr/code/grammars-v4/java8/JavaListener.java
/Users/parrt/antlr/code/grammars-v4/java8/JavaParser.java
/Users/parrt/antlr/code/grammars-v4/java8/Test.java
Total lexer+parser time 1867ms.
 */
public class SimpleTest {

    public static void parseFile(String f) {
        try {
            // Create a scanner that reads from the input stream passed to us
            Lexer lexer = new Java8Lexer(new ANTLRFileStream(f));

            CommonTokenStream tokens = new CommonTokenStream(lexer);
            //			long start = System.currentTimeMillis();
            //			tokens.fill(); // load all and check time
            //			long stop = System.currentTimeMillis();
            //			lexerTime += stop-start;

            // Create a parser that reads from the scanner
            Java8Parser parser = new Java8Parser(tokens);

            // start parsing at the compilationUnit rule
            ParserRuleContext t = parser.compilationUnit();
            for (int i = 0; i < t.getChildCount(); i++) {
                printClass(t.getChild(i));
            }
            Java8Parser.TypeDeclarationContext type = t.getChild(Java8Parser.TypeDeclarationContext.class, 0);//second param is the index of dest element
            printClass(type);
            Java8Parser.ClassDeclarationContext classDeclarationContext = type.classDeclaration();
            Java8Parser.NormalClassDeclarationContext normalClassDeclarationContext = classDeclarationContext.normalClassDeclaration();
            Java8Parser.ClassBodyContext classBodyContext = normalClassDeclarationContext.classBody();
            List<Java8Parser.ClassBodyDeclarationContext> classBodyDeclarationContexts = classBodyContext.classBodyDeclaration();
            for (Java8Parser.ClassBodyDeclarationContext classBodyDeclarationContext : classBodyDeclarationContexts) {
                Java8Parser.ClassMemberDeclarationContext classMemberDeclarationContext = classBodyDeclarationContext.classMemberDeclaration();
                if (classMemberDeclarationContext != null) {
                    Java8Parser.FieldDeclarationContext fieldDeclarationContext = classMemberDeclarationContext.fieldDeclaration();
                    System.out.println(fieldDeclarationContext.getText());
                }
            }

        } catch (Exception e) {
            System.err.println("parser exception: " + e);
            e.printStackTrace();   // so we can get stack trace
        }
    }

    @org.junit.Test
    public void sdf() {
        parseFile("/Users/psyco/workspace/dp/hui-order/hui-order-service/src/main/java/com/dianping/hui/order/entity/OrderDetailEntity.java");
    }

    @org.junit.Test
    public void sdfsdf() throws Exception {
        String s = "/Users/psyco/workspace/dp/hui-order/hui-order-service/src/main/java/com/dianping/hui/order/entity/OrderDetailEntity.java";
        JavaBean a = new JavaBeanDecoder().parse(IOUtils.toString(new FileInputStream(s)));
        printClass(a);

    }

    @org.junit.Test
    public void sdfsdfsd() throws Exception {
        String s = "/Users/psyco/workspace/dp/hui-order/hui-order-service/src/main/java/com/dianping/hui/order/entity/OrderDetailEntity.java";
        JavaBean a = new JavaBeanDecoder().parse(IOUtils.toString(new FileInputStream(s)));
        printClass(a);

    }

    private static void printClass(Object object) {
        System.out.println(ReflectionToStringBuilder.toString(object, ToStringStyle.JSON_STYLE));
    }

    public static void main(String[] args) throws Exception {
        // parse a file
        CompilationUnit cu = JavaParser.parse(new File("/Users/psyco/workspace/dp/hui-order/hui-order-service/src/main/java/com/dianping/hui/order/entity/OrderDetailEntity.java"));

        // visit and change the methods names and parameters
        new MethodChangerVisitor().visit(cu, null);

        // prints the changed compilation unit
        System.out.println(cu);

        System.out.println(getFields(cu));
        System.out.println("----------");
        System.out.println(getFields(cu).get(0).createGetter());
        System.out.println("==================");
        System.out.println(genConvertMethod(cu.getType(0), "Out"));
    }

    private static List<FieldDeclaration> getFields(CompilationUnit cu) {
        if (CollectionUtils.isEmpty(cu.getTypes()) || cu.getTypes().size() < 1)
            return Lists.newLinkedList();
        return getFields(cu.getType(0));
    }

    private static List<FieldDeclaration> getFields(TypeDeclaration<?> cu) {
        return cu.getFields().stream().filter(f -> isProperty(f)).collect(Collectors.toList());
    }


    private static boolean isProperty(FieldDeclaration n) {
        return !n.isStatic() && !n.isTransient();
    }

    public static String getFieldName(FieldDeclaration fieldDeclaration) {
        return fieldDeclaration.getVariable(0).getNameAsString();
    }

    public static String getFieldGetterName(FieldDeclaration fieldDeclaration) {
        return camal("get", getFieldName(fieldDeclaration));
    }

    public static String getFieldSetterName(FieldDeclaration fieldDeclaration) {
        return camal("set", getFieldName(fieldDeclaration));
    }

    public static String camal(String s, String... ss) {
        if (ss == null || ss.length == 0)
            return s.toLowerCase();
        return s.toLowerCase() + Lists.newArrayList(ss).stream().map(e -> StringUtils.capitalize(e)).collect(Collectors.joining());
    }

    public static void genGetter(FieldDeclaration fieldDeclaration) {
        MethodDeclaration method = new MethodDeclaration(EnumSet.of(Modifier.PUBLIC), VOID_TYPE, "get" + StringUtils.capitalize(getFieldName(fieldDeclaration)));


        // add a body to the method
        BlockStmt block = new BlockStmt();
        method.setBody(block);

        // add a statement do the method body
        NameExpr clazz = new NameExpr("System");
        FieldAccessExpr field = new FieldAccessExpr(clazz, "out");
        MethodCallExpr call = new MethodCallExpr(field, "println");
        call.addArgument(new StringLiteralExpr("Hello World!"));
        block.addStatement(call);


    }

    @org.junit.Test
    public void test() {
        /**a.set(b.get())*/
        NameExpr a = new NameExpr("a");
        NameExpr b = new NameExpr("b");
        MethodCallExpr methodCallExpr = new MethodCallExpr(a, "set").addArgument(new MethodCallExpr(b, "get"));
        System.out.println(methodCallExpr);
    }

    public static MethodDeclaration genConvertMethod(TypeDeclaration<?> typeDeclaration, String otherClass) {
        MethodDeclaration method = new MethodDeclaration(EnumSet.of(Modifier.PUBLIC), new ClassOrInterfaceType(StringUtils.capitalize(otherClass)), camal("build", otherClass));
        BlockStmt blockStmt = new BlockStmt();
        getFields(typeDeclaration).forEach(e -> blockStmt.addStatement(convertStatement(e, StringUtils.uncapitalize(otherClass))));
        return method.setBody(blockStmt);
    }

    /**
     * a.set(b.get())
     */
    public static MethodCallExpr convertStatement(FieldDeclaration fieldDeclaration, String destVar) {
        NameExpr a = new NameExpr(getFieldName(fieldDeclaration));
        NameExpr b = new NameExpr(destVar);
        return new MethodCallExpr(a, getFieldSetterName(fieldDeclaration)).addArgument(new MethodCallExpr(b, getFieldGetterName(fieldDeclaration)));
    }

    public static void statement(Expression var, String method, Expression... expressions) {
        FieldAccessExpr field = new FieldAccessExpr(var, method);
        MethodCallExpr call = new MethodCallExpr(field, "println");
        call.addArgument(new StringLiteralExpr("Hello World!"));
    }

    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes.
     */
    private static class MethodChangerVisitor extends VoidVisitorAdapter {
        @Override
        public void visit(MethodDeclaration n, Object arg) {
            ArrayList<Modifier> modifiers = Lists.newArrayList(n.getModifiers());
            if (modifiers.contains(Modifier.STATIC) || modifiers.contains(Modifier.TRANSIENT))
                return;

            // change the name of the method to upper case

            n.setName(n.getNameAsString().toUpperCase());

            // add a new parameter to the method
            n.addParameter("int", "value");
        }
    }

    private static class MethodVistor extends GenericVisitorAdapter<String, String> {
        @Override
        public String visit(FieldDeclaration n, String arg) {
            System.out.println("sdfs---> " + JSON.toJSONString(Lists.newArrayList(n.getVariable(0).getNameAsString(), arg)));
            String visit = super.visit(n, arg);
            System.out.println(visit);
            return visit;
        }

    }
}

