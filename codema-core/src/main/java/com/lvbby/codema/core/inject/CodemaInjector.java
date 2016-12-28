package com.lvbby.codema.core.inject;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.CodemaMachine;
import org.apache.commons.collections.CollectionUtils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2016/12/26.
 */
public class CodemaInjector {

    private List<CodemaInjectorProcessor> injectorProcessors = Lists.newArrayList();

    public CodemaInjector() {
        ServiceLoader.load(CodemaInjectorProcessor.class).forEach(parameterFilterInjectProcessor -> injectorProcessors.add(parameterFilterInjectProcessor));
    }

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

    public CodemaInjector addCodemaInjectorProcessor(CodemaInjectorProcessor codemaInjectorProcessor) {
        if (codemaInjectorProcessor != null)
            injectorProcessors.add(codemaInjectorProcessor);
        return this;
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
            //从context中找到参数, by type
            List<InjectEntry> entries = InjectEntry.from(context, codeRunnerMethod);
            //not match
            if (CollectionUtils.isEmpty(entries))
                return null;
            if (CollectionUtils.isNotEmpty(injectorProcessors)) {
                CodemaInjectContext con = new CodemaInjectContext();
                con.setArgs(entries);
                con.setCodeRunnerMethod(codeRunnerMethod);
                con.setTarget(target);
                con.setContext(context);
                try {
                    for (CodemaInjectorProcessor injectorProcessor : injectorProcessors) {
                        injectorProcessor.process(con);
                    }
                } catch (InjectInterruptException e) {
                    //interrupt detected , skip the rest
                    return null;
                }
            }

            Object[] inject = entries.stream().map(injectEntry -> injectEntry.getValue()).toArray();
            //invoke the delegate method
            try {
                return codeRunnerMethod.invoke(target, inject);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

}