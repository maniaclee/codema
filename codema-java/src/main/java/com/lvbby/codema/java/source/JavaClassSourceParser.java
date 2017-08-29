package com.lvbby.codema.java.source;

import com.lvbby.codema.core.source.SourceLoader;
import com.lvbby.codema.java.entity.JavaClass;

/**
 * Created by lipeng on 17/1/9.
 */
public class JavaClassSourceParser implements SourceLoader<JavaClass> {

    private Class clz;

    private JavaClassSourceParser(Class clz) {
        this.clz = clz;
    }

    public static JavaClassSourceParser fromClass(Class clz) throws Exception {
        return new JavaClassSourceParser(clz);
    }

    @Override
    public JavaClass loadSource() throws Exception {
        return JavaClass.from(clz);
    }
}
