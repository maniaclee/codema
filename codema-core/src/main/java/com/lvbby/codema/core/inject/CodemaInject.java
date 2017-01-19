package com.lvbby.codema.core.inject;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.CodemaMachine;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.error.CodemaRuntimeException;
import com.lvbby.codema.core.utils.CodemaComparator;
import org.apache.commons.collections.CollectionUtils;

import java.beans.Introspector;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2016/12/26.
 */
public class CodemaInject {

    private Set<CodemaInjector> injectorProcessors = Sets.newTreeSet(CodemaComparator.instance);

    public CodemaInject() {
        ServiceLoader.load(CodemaInjector.class).forEach(parameterFilterInjectProcessor -> addCodemaInjectorProcessor(parameterFilterInjectProcessor));
    }

    public List<CodemaMachine> toCodemaMachine(Object a) {
        try {
            return Lists.newArrayList(Introspector.getBeanInfo(a.getClass(), Object.class).getMethodDescriptors()).stream()
                    .filter(m -> m.getMethod().isAnnotationPresent(CodemaRunner.class))
                    .map(methodDescriptor -> wrap2codemaMachine(a, methodDescriptor.getMethod()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("failed to convert to codemaMachine");
        }
    }

    public CodemaInject addCodemaInjectorProcessor(CodemaInjector codemaInjector) {
        if (codemaInjector != null)
            injectorProcessors.add(codemaInjector);
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
            if(method.getName().equals("toString"))
                return String.format("%s.%s",target,codeRunnerMethod.getName());
            if (method.getName().equals("getConfigType"))
                return Optional.ofNullable(codeRunnerMethod.getAnnotation(ConfigBind.class)).map(ConfigBind::value).orElseThrow(() -> new CodemaRuntimeException("config not found "));
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
                    for (CodemaInjector injectorProcessor : injectorProcessors) {
                        injectorProcessor.process(con);
                    }
                } catch (InjectInterruptException e) {
                    //interrupt detected , skip the rest
                    //                    e.printStackTrace();
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
