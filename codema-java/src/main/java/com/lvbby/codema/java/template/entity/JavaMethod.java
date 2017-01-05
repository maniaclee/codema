package com.lvbby.codema.java.template.entity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 17/1/5.
 */
public class JavaMethod {
    public String name;
    public String returnType;
    public List<JavaArg> args;

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
