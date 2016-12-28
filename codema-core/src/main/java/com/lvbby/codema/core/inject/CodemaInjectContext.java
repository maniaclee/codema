package com.lvbby.codema.core.inject;

import com.lvbby.codema.core.CodemaContext;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 16/12/27.
 */
public class CodemaInjectContext {
    private Object target;
    private Method codeRunnerMethod;
    private List<InjectEntry> args;
    private CodemaContext context;

    public CodemaContext getContext() {
        return context;
    }

    public void setContext(CodemaContext context) {
        this.context = context;
    }

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

    public Object invoke() throws Exception {
        return codeRunnerMethod.invoke(target, args.stream().map(InjectEntry::getValue).toArray());
    }

    public Object invoke(List<InjectEntry> injectEntries) throws Exception {
        return codeRunnerMethod.invoke(target, injectEntries.stream().map(InjectEntry::getValue).toArray());
    }

    public List<InjectEntry> cloneEntries() {
        return args.stream().map(injectEntry -> injectEntry.copy()).collect(Collectors.toList());
    }
}
