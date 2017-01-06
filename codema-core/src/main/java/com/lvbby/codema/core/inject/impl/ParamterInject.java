package com.lvbby.codema.core.inject.impl;

import com.lvbby.codema.core.inject.*;
import com.lvbby.codema.core.bean.CodemaBean;
import com.lvbby.codema.core.utils.JavaUtils;
import com.lvbby.codema.core.utils.OrderValue;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lipeng on 2017/1/1.
 */
@OrderValue(100)
public class ParamterInject implements CodemaInjector {

    @Override
    public void process(CodemaInjectContext context) throws Exception {
        context.getArgs().stream().filter(injectEntry -> injectEntry.getParameter().isAnnotationPresent(Parameter.class)).forEach(injectEntry -> {
            Parameter annotation = injectEntry.getParameter().getAnnotation(Parameter.class);
            if (StringUtils.isBlank(annotation.value()))
                return;
            String beanId = null;
            try {
                beanId = evalBeanId(context, annotation.value());
            } catch (Exception e) {
                throw new InjectInterruptException(e);
            }
            Object bean = context.getContext().getCodema().getCodemaBeanFactory().getBean(beanId);
            if (bean == null && annotation.createFactory() != null) {
                InjectParameterFactory instance = JavaUtils.instance(annotation.createFactory());
                CodemaBean codemaBean = instance.create(context, beanId);
                if (codemaBean != null) {
                    bean = codemaBean.getResource();
                    //注册到容器
                    context.getContext().getCodema().getCodemaBeanFactory().register(codemaBean);
                }
            }
            //注入参数bean
            injectEntry.setValue(bean);
        });
    }

    /***
     * 解析${expr} 表达式,获取最终的beanId
     */
    private String evalBeanId(CodemaInjectContext context, String value) {
        if (value.startsWith("$")) {
            Matcher matcher = Pattern.compile("\\$\\{([^\\{\\}]+)\\}").matcher(value);
            if (!matcher.find())
                throw new IllegalArgumentException(String.format("illegal parameter express [%s] , please use ${expression}", value));
            value = matcher.group(1);
        }
        Matcher matcher = Pattern.compile("[^A-Z]+[A-Z][^\\.]+").matcher(value);
        if (!matcher.find())
            throw new IllegalArgumentException(String.format("illegal parameter express [%s] , please use ${expression}", value));
        int end = matcher.end(0);
        if (end < value.length() - 1) {
            String parentBeanId = value.substring(0, end);
            Object parentBean = null;
            try {
                parentBean = context.getContext().getConfig(Class.forName(parentBeanId));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (parentBean == null)
                throw new IllegalArgumentException(String.format("illegal bean id [%s]", parentBeanId));
            for (String p : value.substring(end + 1).split("\\.")) {
                try {
                    parentBean = JavaUtils.getProperty(parentBean, p);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new IllegalArgumentException(String.format("error parsing bean id express[%s]", parentBeanId), e);
                }
            }
            if (!(parentBean instanceof String))
                throw new IllegalArgumentException(String.format("error parsing bean id express[%s]", parentBeanId));

            return (String) parentBean;
        }
        return value;
    }

}
