package com.lvbby.codema.tool.coder.source.handler;

import com.lvbby.codema.coder.TypedCoderHandler;
import com.lvbby.codema.tool.coder.source.JavaSourceCoderRequest;

/**
 * Created by lipeng on 16/12/21.
 */
public class JavaSourceRepositotyCoder extends TypedCoderHandler<JavaSourceCoderRequest> {
    @Override
    public void process(JavaSourceCoderRequest request) throws Exception {
        if (request.getTypeDeclaration()==null) return;

    }
}
