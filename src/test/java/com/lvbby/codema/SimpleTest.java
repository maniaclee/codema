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

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.bean.JavaBean;
import com.lvbby.codema.bean.JavaBeanDecoder;
import com.lvbby.codema.parser.java8.Java8Lexer;
import com.lvbby.codema.parser.java8.Java8Parser;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static com.lvbby.codema.lexer.JavaLexer.getFields;
import static com.lvbby.codema.tool.JavaConvertMethod.genConvertFromMethod;
import static com.lvbby.codema.tool.JavaConvertMethod.genConvertToMethod;

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

        // prints the changed compilation unit
        System.out.println(cu);

        System.out.println(getFields(cu));
        System.out.println("----------");
        System.out.println(getFields(cu).get(0).createGetter());
        System.out.println("==================");
        System.out.println(genConvertToMethod(cu.getType(0), "Out"));
        System.out.println(genConvertFromMethod(cu.getType(0), "Out"));

        //        VariableDeclarationExpr parse  = new VariableDeclarationExpr(new ClassOrInterfaceType("String"),"a").set;
        //        System.out.println(parse);
    }
}

