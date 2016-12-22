package com.lvbby.codema.coder.java.source.handler;

import com.lvbby.codema.lexer.JavaLexer;
import com.lvbby.codema.coder.TypedCoderHandler;
import com.lvbby.codema.coder.java.source.JavaSourceCoderRequest;

/**
 * Created by lipeng on 2016/12/20.
 */
public class JavaSourceParserCoder extends TypedCoderHandler<JavaSourceCoderRequest> {

    @Override
    public void process(JavaSourceCoderRequest context) throws Exception {
        context.setTypeDeclaration(JavaLexer.parseJavaClass(context.getSource()));
    }
}
