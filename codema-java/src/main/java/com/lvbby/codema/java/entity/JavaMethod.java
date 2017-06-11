package com.lvbby.codema.java.entity;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.google.common.collect.Lists;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.tool.JavaCodeUtils;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 17/1/5.
 */
public class JavaMethod {
    public String name;
    public JavaType returnType;
    public List<JavaArg> args;
    private transient MethodDeclaration src;


    public static List<JavaMethod> from(Class clz) {
        return ReflectionUtils.getAllMethods(clz).stream().map(method -> from(method)).collect(Collectors.toList());
    }

    public static JavaMethod from(Method m) {
        JavaMethod javaMethod = new JavaMethod();
        javaMethod.setName(m.getName());
        javaMethod.setReturnType(JavaType.ofMethodReturnType(m));
        javaMethod.setArgs(Lists.newArrayList(m.getParameters()).stream().map(parameterDescriptor -> {
            JavaArg javaArg = new JavaArg();
            javaArg.setName(parameterDescriptor.getName());
            javaArg.setType(JavaType.ofMethodParameter(parameterDescriptor));
            return javaArg;
        }).collect(Collectors.toList()));
        return javaMethod;
    }

    public JavaMethod src(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        setSrc(JavaCodeUtils.getMethodSrc(classOrInterfaceDeclaration, this));
        return this;
    }


    public boolean returnVoid() {
        return returnType.beVoid();
    }

    /**
     * (arg1,arg2)  -->  arg1,arg2
     */
    public String getArgsInvoke() {
        return Optional.ofNullable(args).map(args -> args.stream().map(JavaArg::getName).collect(Collectors.joining(","))).orElse("");
    }

    public String getArgsSignature() {
        return CollectionUtils.isEmpty(getArgs()) ? "" : getArgs().stream().map(javaArg -> javaArg.getType() + " " + javaArg.getName()).collect(Collectors.joining(","));
    }

    public String getArgsDefaultValue() {
        return getArgs().stream().map(JavaArg::getDefaultValue).collect(Collectors.joining(","));
    }

    public String loadParameterSignature() {
        return args == null ? "" : args.stream().map(javaArg -> javaArg.getType() + " " + javaArg.getName()).collect(Collectors.joining(","));
    }

    public String loadParameter() {
        return args == null ? "" : args.stream().map(javaArg -> javaArg.getName()).collect(Collectors.joining(","));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<JavaArg> getArgs() {
        return args;
    }

    public void setArgs(List<JavaArg> args) {
        this.args = args;
    }

    public JavaType getReturnType() {
        return returnType;
    }

    public void setReturnType(JavaType returnType) {
        this.returnType = returnType;
    }

    public MethodDeclaration getSrc() {
        return src;
    }

    public void setSrc(MethodDeclaration src) {
        this.src = src;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JavaMethod that = (JavaMethod) o;

        if (!name.equals(that.name)) return false;
        return CollectionUtils.isEqualCollection(that.getArgs(), getArgs());
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + args.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("[%s %s(%s)]",
                returnType,
                getName(),
                (CollectionUtils.isEmpty(args) ? "" : args.stream().map(JavaArg::toString).collect(Collectors.joining(","))));
    }
}
