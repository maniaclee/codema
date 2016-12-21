package com.lvbby.codema.coder.java;

import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.coder.TypedCoderHandler;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by lipeng on 16/12/21.
 * 最先调用的coderHandler,初始化package
 */
public class JavaInitCoder extends TypedCoderHandler<JavaCoderRequest> {
    @Override
    public void process(JavaCoderRequest request) throws Exception {
        CompilationUnit re = new CompilationUnit();
        if (StringUtils.isNotBlank(request.getPack()))
            re.setPackage(request.getPack());
        //init CompilationUnit
        request.setResult(re);
    }
}
