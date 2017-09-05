package com.lvbby.codema.java.source;

import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.core.source.SourceLoader;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaClassUtils;
import com.lvbby.codema.java.tool.JavaSrcLoader;

/**
 * Created by lipeng on 17/1/9.
 */
public class JavaClassSourceParser implements SourceLoader<JavaClass> {

    private JavaClass clz;

    private JavaClassSourceParser(JavaClass clz) {
        this.clz = clz;
    }

    public static JavaClassSourceParser fromClass(Class clz) throws Exception {
        return new JavaClassSourceParser(JavaClass.from(clz));
    }

    /***
     * 从项目目录里按照全类名加载
     * @param classFullName
     * @return
     * @throws Exception
     */
    public static JavaClassSourceParser fromClassFullName(String classFullName) throws Exception {
        return fromClassSrc(JavaSrcLoader.getJavaSrcCompilationUnit(classFullName));
    }

    public static JavaClassSourceParser fromClassSrc(CompilationUnit compilationUnit) throws Exception {
        return new JavaClassSourceParser(
                JavaClassUtils.convert(compilationUnit));
    }

    @Override
    public JavaClass loadSource() throws Exception {
        return clz;
    }
}
