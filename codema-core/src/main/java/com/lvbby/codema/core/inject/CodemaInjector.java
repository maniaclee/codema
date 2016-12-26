package com.lvbby.codema.core.inject;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.CodemaMachine;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
            return codeRunnerMethod.invoke(target, inject);
        }
    }

    private Object[] inject(CodemaContext context, Method method) {
        return injectByClasses(context, Arrays.asList(method.getParameters()).stream().map(parameter -> parameter.getType()).collect(Collectors.toList()));
    }

    private Object[] injectByClasses(CodemaContext context, List<Class> args) {
        if (CollectionUtils.isEmpty(args))
            return null;
        return args.stream().map(arg -> inject(context, arg)).toArray();
    }

    private Object inject(CodemaContext context, Class<?> clz) {
        if (CodemaContext.class.equals(clz))
            return context;
        if (Objects.equals(context.getSource().getClass(), clz))
            return context.getSource();
        return ObjectUtils.firstNonNull(context.getParam(clz).orElse(null), context.getConfig(clz));
    }
}
