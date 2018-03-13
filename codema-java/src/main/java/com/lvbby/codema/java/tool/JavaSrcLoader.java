package com.lvbby.codema.java.tool;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Created by lipeng on 2017/1/1.
 */
public class JavaSrcLoader {

    public static List<MavenConfig> mavenDirectoryScanner;

    /***
     * 初始化maven扫描路径
     * @param javaFileSrcRoot
     */
    public static void initJavaSrcRoots(List<File> javaFileSrcRoot) {
        initJavaSrcRoots(javaFileSrcRoot, 5);
    }

    /**
     * 初始化maven扫描路径
     * @param javaFileSrcRoot
     * @param depth 0:不限层数
     */
    public static void initJavaSrcRoots(List<File> javaFileSrcRoot, int depth) {
        synchronized (JavaSrcLoader.class) {
            mavenDirectoryScanner = Lists.newArrayList();
            for (MavenConfig mavenConfig : MavenConfig.scan(javaFileSrcRoot, depth)) {
                mavenDirectoryScanner.addAll(mavenConfig.toList());
            }
        }
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
    public static CompilationUnit loadJavaSrcFromProject(Class clz) throws Exception {
        return loadJavaSrcFromProject(clz.getName());
    }

    /**
     * 从外部maven项目里查找java文件
     *
     * @param className
     * @return
     * @throws Exception
     */
    public static CompilationUnit loadJavaSrcFromProject(String className) throws Exception {
        String s = loadJavaSrcFromProjectAsString(className);
        if(StringUtils.isNotBlank(s)){
            return JavaLexer.read(s);
        }
        return null;
    }

    public static String loadJavaSrcFromProjectAsString(String className)  {
        ServiceLoader<IJavaSourceLoader> loader = ServiceLoader.load(IJavaSourceLoader.class);
        for(Iterator<IJavaSourceLoader> i = loader.iterator();i.hasNext();){
            String source = null;
            try {
                source = i.next().loadJavaSource(className);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if(StringUtils.isNotBlank(source)){
                return source;
            }
        }
        if (CollectionUtils.isNotEmpty(mavenDirectoryScanner)) {
            for (MavenConfig mavenConfig : mavenDirectoryScanner) {
                File re = new File(mavenConfig.getDestSrcRoot(), className.replace('.', '/') + ".java");
                if (re.isFile() && re.exists()) {
                    try {
                        return IOUtils.toString(new FileInputStream(re));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
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
