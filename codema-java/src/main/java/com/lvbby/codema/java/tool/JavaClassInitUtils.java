package com.lvbby.codema.java.tool;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.baisc.JavaSourceParam;
import com.lvbby.codema.java.lexer.JavaLexer;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2016/12/25.
 */
public class JavaClassInitUtils {
    public List<CompilationUnit> process(CodemaContext request, JavaBasicCodemaConfig config) throws Exception {
        return request.getParam(JavaSourceParam.class)
                .map(e -> e.getCompilationUnits())
                .map(compilationUnits -> compilationUnits.stream().map(compilationUnit -> {
                    CompilationUnit re = new CompilationUnit();
                    //package
                    if (StringUtils.isNotBlank(config.getDestPackage()))
                        re.setPackage(config.getDestPackage());
                    //class
                    ClassOrInterfaceDeclaration clz = createClass(request, config, JavaLexer.getClass(compilationUnit).map(cu -> cu.getNameAsString() + "Test").orElse(null));
                    clz.setParentNode(re);
                    re.setTypes(NodeList.nodeList(clz));
                    return re;
                }).collect(Collectors.toList())).orElse(Lists.newLinkedList());
    }

    public static ClassOrInterfaceDeclaration createClass(CodemaContext request, JavaBasicCodemaConfig config, String className) {
        if (StringUtils.isBlank(className))
            className = "Untitled" + UUID.randomUUID();
        else
            className += config.getDestClassName();

        ClassOrInterfaceDeclaration re = new ClassOrInterfaceDeclaration(EnumSet.of(Modifier.PUBLIC), false, className).setInterface(config.isToBeInterface());
        if (request.loadConfig(JavaBasicCodemaConfig.class).isPresent())
            re.setJavaDocComment(String.format("\n* Created by %s on %s\n", request.loadConfig(JavaBasicCodemaConfig.class), new SimpleDateFormat("yyyy/MM/hh").format(new Date())));
        return re;
    }
}
