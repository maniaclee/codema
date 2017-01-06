package com.lvbby.codema.java.resource;

import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.core.bean.CodemaBean;
import com.lvbby.codema.java.tool.JavaLexer;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by lipeng on 2016/12/31.
 */
public class JavaCodemaResoure extends CodemaBean {
    public JavaCodemaResoure(CompilationUnit compilationUnit) {
        super(compilationUnit);
        String className = JavaLexer.getClass(compilationUnit).map(clz -> clz.getNameAsString()).orElse("");
        String id = compilationUnit.getPackage().map(packageDeclaration -> packageDeclaration.getPackageName())
                .filter(e -> StringUtils.isNotBlank(e))
                .map(e -> e + "." + className).orElse(className);
        setId(id);
        initType(false, false);
    }
}
