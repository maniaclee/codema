package com.lvbby.codema.java.result;

import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.ResultHandler;
import com.lvbby.codema.java.resource.JavaCodemaResoure;

/**
 * Created by lipeng on 2016/12/31.
 * 将结果注册到容器中
 */
public class JavaRegisterResultHandler implements ResultHandler {
    @Override
    public void handle(CodemaContext codemaContext, Object result) {
        if (result instanceof CompilationUnit)
            codemaContext.getCodema().getResourceLoader().register(new JavaCodemaResoure((CompilationUnit) result));
    }
}
