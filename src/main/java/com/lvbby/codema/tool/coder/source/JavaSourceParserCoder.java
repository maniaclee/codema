package com.lvbby.codema.tool.coder.source;

import com.lvbby.codema.lexer.JavaLexer;
import com.lvbby.codema.tool.coder.JavaSourceCoderRequest;
import com.lvbby.codema.tool.coder.TypedCoderHandler;

/**
 * Created by lipeng on 2016/12/20.
 */
public class JavaSourceParserCoder extends TypedCoderHandler<JavaSourceCoderRequest> {

    @Override
    public void process(JavaSourceCoderRequest context) throws Exception {
        context.setTypeDeclaration(JavaLexer.parseJavaClass(context.getSource()));
    }
}
