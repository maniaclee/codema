package com.lvbby.codema.java.tool;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lipeng on 2017/1/1.
 */
public class JavaSrcLoader {

    public static MethodDeclaration getMethod(Class clz, String methodName) {
        CompilationUnit javaSrcCompilationUnit = getJavaSrcCompilationUnit(clz);
        return JavaLexer.getMethods(JavaLexer.getClass(javaSrcCompilationUnit).orElseThrow(() -> new IllegalArgumentException("no class found ")), null).stream()
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

    /***
     * maven 将所有源文件打包到target/classes/下，然后获取resources的方式获取源文件
     * @param clz
     * @return
     */
    public static InputStream getJavaSrc(Class clz) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(String.format("%s.java", clz.getName().replace('.', '/')));
    }

}
