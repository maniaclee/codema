package com.lvbby.codema.java.source;

import com.lvbby.codema.core.source.SourceLoader;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaClassUtils;
import com.lvbby.codema.java.tool.JavaLexer;
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
        return new JavaClassSourceParser(
                JavaClassUtils.convert(JavaSrcLoader.getJavaSrcCompilationUnit(classFullName)));
    }

    @Override
    public JavaClass loadSource() throws Exception {
        return clz;
    }
}
