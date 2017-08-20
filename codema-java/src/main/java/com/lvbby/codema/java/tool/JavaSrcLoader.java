package com.lvbby.codema.java.tool;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Created by lipeng on 2017/1/1.
 */
public class JavaSrcLoader {


    private static MavenDirectoryScanner mavenDirectoryScanner;

    public static void initJavaSrcRoots(List<File> javaFileSrcRoot) {
        initJavaSrcRoots(javaFileSrcRoot, 5);
    }

    public static void initJavaSrcRoots(List<File> javaFileSrcRoot, int depth) {
        mavenDirectoryScanner = new MavenDirectoryScanner(javaFileSrcRoot, depth);
    }

    public static MethodDeclaration getMethod(Class clz, String methodName) {
        CompilationUnit javaSrcCompilationUnit = getJavaSrcCompilationUnit(clz);
        return JavaLexer.getMethods(JavaLexer.getClass(javaSrcCompilationUnit).orElseThrow(() -> new IllegalArgumentException("no class found ")), null).stream()
                .filter(methodDeclaration -> methodDeclaration.getNameAsString().equals(methodName)).findFirst().orElse(null);
    }

    /***
     * 加载java源代码
     * @param clz
     * @return
     */
    public static CompilationUnit getJavaSrcCompilationUnit(Class clz) {
        try {
            //1. 试着从maven外部项目里查找
            CompilationUnit compilationUnit = loadJavaSrcFromProject(clz);
            if (compilationUnit != null)
                return compilationUnit;
            //2. 试着从内部项目里查找
            return JavaLexer.read(IOUtils.toString(loadJavaSrcFromInnerProject(clz)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(String.format("can't find class src for class : %s", clz.getName()), e);
        }
    }

    public static CompilationUnit getJavaSrcCompilationUnit(String className) {
        try {
            //从maven外部项目里查找
            return loadJavaSrcFromProject(className);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(String.format("can't find class src for class : %s", className), e);
        }
    }

    /**
     * 从外部maven项目里查找java文件
     *
     * @param clz
     * @return
     * @throws Exception
     */
    private static CompilationUnit loadJavaSrcFromProject(Class clz) throws Exception {
        return loadJavaSrcFromProject(clz.getName());
    }

    /**
     * 从外部maven项目里查找java文件
     *
     * @param className
     * @return
     * @throws Exception
     */
    private static CompilationUnit loadJavaSrcFromProject(String className) throws Exception {
        for (File file : mavenDirectoryScanner.getMavenSrcDirectories()) {
            File re = new File(file, className.replace('.', '/') + ".java");
            if (re.isFile() && re.exists()) {
                return JavaLexer.read(re);
            }
        }
        return null;
    }


    /***
     * maven 将所有源文件打包到target/classes/下，然后获取resources的方式获取源文件
     * @param clz
     * @return
     */
    private static InputStream loadJavaSrcFromInnerProject(Class clz) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(String.format("%s.java", clz.getName().replace('.', '/')));
    }

}
