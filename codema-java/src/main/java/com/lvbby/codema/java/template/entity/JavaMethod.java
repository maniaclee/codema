package com.lvbby.codema.java.template.entity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 17/1/5.
 */
public class JavaMethod {
    public String name;
    public String returnType;
    public List<JavaArg> args;
    public String argsDefaultValue;
    public String argsSignature;

    public boolean isVoid() {
        return Objects.equals("void", returnType);
    }

    /**
     * (arg1,arg2)  -->  arg1,arg2
     */
    public String getArgsInvoke() {
        return Optional.ofNullable(args).map(args -> args.stream().map(JavaArg::getName).collect(Collectors.joining(","))).orElse("");
    }

    public String getArgsSignature() {
        return argsSignature;
    }

    public void setArgsSignature(String argsSignature) {
        this.argsSignature = argsSignature;
    }

    public String getArgsDefaultValue() {
        return argsDefaultValue;
    }

    public void setArgsDefaultValue(String argsDefaultValue) {
        this.argsDefaultValue = argsDefaultValue;
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

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<JavaArg> getArgs() {
        return args;
    }

    public void setArgs(List<JavaArg> args) {
        this.args = args;
    }
}
