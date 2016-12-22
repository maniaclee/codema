package com.lvbby.codema.coder.java;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.lvbby.codema.coder.TypedCoderHandler;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.UUID;

/**
 * Created by lipeng on 2016/12/20.
 * 生成新的class
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
        String className = request.getClassName();
        if (StringUtils.isBlank(className))
            className = "Untitled" + UUID.randomUUID();
        ClassOrInterfaceDeclaration re = new ClassOrInterfaceDeclaration(EnumSet.of(Modifier.PUBLIC), false, className)
                .setInterface(request.isInterface());
        re.setJavaDocComment(String.format("\n* Created by %s on %s\n", request.getAuthor(), new SimpleDateFormat("yyyy/MM/hh").format(new Date())));
        return re;
    }
}
