package com.lvbby.codema.tool.coder;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;

/**
 * Created by lipeng on 2016/12/20.
 */
public class JavaClassCoder extends TypedCoderHandler<JavaCoderRequest> {
    @Override
    public void process(JavaCoderRequest context) throws Exception {
        ClassOrInterfaceDeclaration re = createClass(context);
        /** set parent */
        re.setParentNode(context.getResult());
        context.getResult().setTypes(NodeList.nodeList(re));
    }

    public static ClassOrInterfaceDeclaration createClass(JavaCoderRequest request) {
        ClassOrInterfaceDeclaration re = new ClassOrInterfaceDeclaration(EnumSet.of(Modifier.PUBLIC), false, request.getClassName())
                .setInterface(request.isInterface());
        re.setBlockComment(String.format("Created by %s on %s", request.getAuthor(), new SimpleDateFormat("yyyy/MM/hh").format(new Date())));
        return re;
    }
}
