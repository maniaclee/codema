package com.lvbby.codema.java.entity;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.beans.MethodDescriptor;
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

    public static JavaMethod from(MethodDescriptor m){
        JavaMethod javaMethod = new JavaMethod();
        javaMethod.setName(m.getName());
        javaMethod.setReturnType(JavaType.ofMethodReturnType(m.getMethod()));
        javaMethod.setArgs(Lists.newArrayList(m.getMethod().getParameters()).stream().map(parameterDescriptor -> {
            JavaArg javaArg = new JavaArg();
            javaArg.setName(parameterDescriptor.getName());
            javaArg.setType(JavaType.ofMethodParameter(parameterDescriptor));
            return javaArg;
        }).collect(Collectors.toList()));
        return javaMethod;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JavaMethod that = (JavaMethod) o;

        if (!name.equals(that.name)) return false;
        return CollectionUtils.isEqualCollection(that.getArgs(),getArgs());
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
