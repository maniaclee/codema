package com.lvbby.codema.core.inject;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by lipeng on 16/12/27.
 */
public class CodemaInjectContext {
    private Object target;
    private Method codeRunnerMethod;
    private List<InjectEntry> args;

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Method getCodeRunnerMethod() {
        return codeRunnerMethod;
    }

    public void setCodeRunnerMethod(Method codeRunnerMethod) {
        this.codeRunnerMethod = codeRunnerMethod;
    }

    public List<InjectEntry> getArgs() {
        return args;
    }

    public void setArgs(List<InjectEntry> args) {
        this.args = args;
    }
}
