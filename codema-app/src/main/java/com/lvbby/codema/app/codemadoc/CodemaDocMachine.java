package com.lvbby.codema.app.codemadoc;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import com.google.common.collect.Lists;
import com.lvbby.codema.core.machine.CommonMachineFactory;
import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.Machine;
import com.lvbby.codema.core.handler.ResultHandlerFactory;
import com.lvbby.codema.core.render.TemplateEngineResult;
import com.lvbby.codema.core.tool.mysql.SqlMachineFactory;
import com.lvbby.codema.core.utils.ClassUtils;
import com.lvbby.codema.java.machine.JavaSourceMachineFactory;
import com.lvbby.codema.java.tool.JavaLexer;
import com.lvbby.codema.java.tool.JavaSrcLoader;
import com.lvbby.codema.java.tool.MavenDirectoryScanner;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自己产生自己的README.md
 * @author dushang.lp
 * @version $Id: CodemaDocMachine.java, v 0.1 2017年12月19日 下午7:57 dushang.lp Exp $
 */
public class CodemaDocMachine extends AbstractBaseMachine<Object,String>{
    @Override protected void doCode() throws Exception {
        List<String> machines = ClassUtils
                .getClassesOfPackage(ClassUtils.getParentPackage(getClass())).stream()
                .filter(aClass -> Machine.class.isAssignableFrom(aClass) && !aClass
                        .isAnonymousClass()).map(Class::getName).collect(Collectors.toList());

        MethodDeclaration main = JavaLexer.getMethodByNameSingle(
                JavaLexer.getClass(JavaSrcLoader.loadJavaSrcFromProject(getClass())).get(), "main");

        handle(new TemplateEngineResult(loadResourceAsString("README.md"))
                .bind("machines",machines)
                .bind("quickStart",main.toString(new PrettyPrinterConfiguration()))
                .bind("resultHandlers", Lists.newArrayList(ResultHandlerFactory.class).stream().map(Class::getName).collect(Collectors.toList()))
                .bind("sources", Lists.newArrayList(
                        JavaSourceMachineFactory.class,
                        SqlMachineFactory.class,
                        CommonMachineFactory.class)
                        .stream().map(Class::getName).collect(Collectors.toList())
                )
                .filePath(getDestRootDir(),"README.md")
        );
    }

    public static void main(String[] args) throws Exception {
        MavenDirectoryScanner mavenDirectoryScanner = new MavenDirectoryScanner(
                Lists.newArrayList(new File(System.getProperty("user.home"),"workspace")));

        File dir = mavenDirectoryScanner.getMavenDirectories().stream()
                .filter(file -> file.getAbsolutePath().endsWith("codema")).findAny().orElse(null);
        System.out.println(dir.getAbsolutePath());

        CodemaDocMachine codemaDocMachine = new CodemaDocMachine();
        codemaDocMachine.setDestRootDir(dir.getAbsolutePath());
        codemaDocMachine
                .addResultHandler(ResultHandlerFactory.fileWrite)
                .addResultHandler(ResultHandlerFactory.print)
                .run();
    }
}