package com.lvbby.codema.tool.coder.source.coder;

import com.lvbby.codema.coder.TypedCoderHandler;
import com.lvbby.codema.tool.coder.source.JavaSourceCoderRequest;

/**
 * Created by lipeng on 16/12/21.
 * append suffix after origin class
 */
public class JavaSourceClassNameCoder extends TypedCoderHandler<JavaSourceCoderRequest> {
    private String suffix;

    public JavaSourceClassNameCoder(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public void process(JavaSourceCoderRequest request) throws Exception {
        request.setClassName(request.getTypeDeclaration().getNameAsString() + suffix);
    }
}
