package com.lvbby.codema.core.inject;

import com.lvbby.codema.core.CodemaContext;
import org.apache.commons.lang3.ObjectUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2016/12/26.
 */
public class InjectEntry {
    private Parameter parameter;
    private Object value;

    public static List<InjectEntry> from(CodemaContext context, Method method) {
        return Arrays.asList(method.getParameters()).stream().map(p -> from(context, p)).collect(Collectors.toList());
    }


    public static boolean usable(List<InjectEntry> injectEntries) {
        return injectEntries.stream().anyMatch(injectEntry -> !(injectEntry.getValue() == null && injectEntry.getParameter().isAnnotationPresent(NotNull.class)));
    }

    public static InjectEntry from(CodemaContext context, Parameter parameter) {
        return new InjectEntry(parameter).value(getInjectValue(context, parameter));
    }

    private InjectEntry(Parameter parameter) {
        this.parameter = parameter;
    }

    private static Object getInjectValue(CodemaContext context, Parameter parameter) {
        InjectEntry re = new InjectEntry(parameter);
        Class<?> clz = parameter.getType();
        if (CodemaContext.class.equals(clz))
            return context;
        if (Objects.equals(context.getSource().getClass(), clz))
            return context.getSource();
        try {
            return ObjectUtils.firstNonNull(context.getParam(clz).orElse(null), context.getConfig(clz));
        } catch (Exception e) {
            return null;
        }
    }

    public InjectEntry value(Object value) {
        this.value = value;
        return this;
    }


    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }
}
