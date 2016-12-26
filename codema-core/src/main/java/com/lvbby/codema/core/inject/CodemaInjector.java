package com.lvbby.codema.core.inject;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.CodemaMachine;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2016/12/26.
 */
public class CodemaInjector {

    public List<CodemaMachine> toCodemaMachine(Object a) {
        try {
            return Lists.newArrayList(Introspector.getBeanInfo(a.getClass(), Object.class).getMethodDescriptors()).stream()
                    .filter(m -> m.getMethod().isAnnotationPresent(CodemaRunner.class))
                    .map(methodDescriptor -> wrap2codemaMachine(a, methodDescriptor.getMethod()))
                    .collect(Collectors.toList());
        } catch (IntrospectionException e) {
            throw new RuntimeException("failed to convert to codemaMachine");
        }
    }


    private CodemaMachine wrap2codemaMachine(Object a, Method m) {
        return (CodemaMachine) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{CodemaMachine.class}, new CodemaRunnerInvocationHandler(a, m));
    }

    private class CodemaRunnerInvocationHandler implements InvocationHandler {
        private Object target;
        private Method codeRunnerMethod;

        public CodemaRunnerInvocationHandler(Object target, Method codeRunnerMethod) {
            this.target = target;
            this.codeRunnerMethod = codeRunnerMethod;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            CodemaContext context = (CodemaContext) args[0];
            Object[] inject = inject(context, codeRunnerMethod);
            //not match
            if (inject == null)
                return null;
            //invoke the delegate method
            return codeRunnerMethod.invoke(target, inject);
        }
    }

    private Object[] inject(CodemaContext context, Method method) {
        List<InjectEntry> from = InjectEntry.from(context, method);
        if (!InjectEntry.usable(from))
            return null;
        return from.stream().map(injectEntry -> injectEntry.getValue()).toArray();
    }

}
