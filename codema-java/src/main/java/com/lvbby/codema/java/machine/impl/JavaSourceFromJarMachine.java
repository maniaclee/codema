package com.lvbby.codema.java.machine.impl;

import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaClassUtils;
import com.lvbby.codema.java.tool.JavaLexer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;

/**
 * 从jar file 路径读取
 * Created by lipeng on 2018/3/11.
 */
public class JavaSourceFromJarMachine extends AbstractBaseMachine<String, JavaClass> {
    @Override
    protected void doCode() throws Exception {
        //加载文件
        String javaSource = IOUtils.toString(getJarFile(javaSrcJarFile(source)));
        //输出结果
        handle(BasicResult.instance(JavaClassUtils.convert(JavaLexer.read(javaSource))));
    }

    private InputStream getJarFile(String path) throws  Exception {
        JarURLConnection jarConnection = (JarURLConnection) new URL(path).openConnection();
        return jarConnection.getInputStream();
    }

    private String  javaSrcJarFile(String source ){
        source.replace("jar:file:","");
        String[] split = source.split("!");
        Validate.isTrue(split.length == 2, "invalid jar url[%s],format should be filepath!javaFilePath", source);
        String javaFilePath = split[1].replace(".class",".java");
        String jarFilePath = split[0];
        if (!jarFilePath.endsWith("-sources.jar")) {
            jarFilePath = jarFilePath.replace(".jar", "-sources.jar");
        }
        return String.format("jar:file:%s!%s", jarFilePath,javaFilePath);
    }
    public static void main(String[] args) throws Exception {
        String jarPath = "/Users/psyco/.m2/repository/org/springframework/spring-context/4.1.7.RELEASE/spring-context-4.1.7.RELEASE.jar!/org/springframework/context/ApplicationContext.class";
        new JavaSourceFromJarMachine()
                .source(jarPath)
                .addResultHandler(result1 -> System.out.println(((JavaClass)result1.getResult()).getSrc()))
                .run();
    }
}
