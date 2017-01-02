package com.lvbby.codema.java.tool;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by lipeng on 2017/1/1.
 */
public class JavaSrcLoader {

    public static MethodDeclaration getMethod(Class clz, String methodName) {
        CompilationUnit javaSrcCompilationUnit = getJavaSrcCompilationUnit(clz);
        return JavaLexer.getMethods(JavaLexer.getClass(javaSrcCompilationUnit).orElseThrow(() -> new IllegalArgumentException("no class found ")),null).stream()
                .filter(methodDeclaration -> methodDeclaration.getNameAsString().equals(methodName)).findFirst().orElse(null);
    }

    public static CompilationUnit getJavaSrcCompilationUnit(Class clz) {
        try {
            return JavaLexer.read(IOUtils.toString(getJavaSrc(clz)));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(String.format("can't find class src for class : %s", clz.getName()), e);
        }
    }

    public static InputStream getJavaSrc(Class clz) {
        try {
            //            URL resource = getClass().getResource("");
            URL resource = JavaSrcLoader.class.getProtectionDomain().getCodeSource().getLocation();
            if (resource.getProtocol().equalsIgnoreCase("file")) {
                //  ~/target/classes/
                File root = new File(JavaSrcLoader.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("target/classes", "src/main/java"));
                File file = new File(root, clz.getName().replace('.', '/') + ".java");
                return new FileInputStream(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
