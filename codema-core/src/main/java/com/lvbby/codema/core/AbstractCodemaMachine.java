package com.lvbby.codema.core;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.core.utils.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 *
 * @author dushang.lp
 * @version $Id: AbstractCodemaMachine.java, v 0.1 2017-08-24 3:32 dushang.lp Exp $
 */
public abstract class AbstractCodemaMachine<T extends CommonCodemaConfig,S,O>
                                           implements CodemaMachine<T,S,O> {
    private Result<O> result;

    @Override public Result<O> getResult() {
        return result;
    }

    protected void setResult(Result<O> result){
        this.result=result;
    }

    protected String loadResourceAsString(String resourceName) throws IOException {
        return ReflectionUtils.loadResource(getClass(), resourceName);
    }

    protected List loadFrom(CodemaContext context, T config) {
        if (config.getFromConfig() != null) {
            //根据config来查找bean
            List<Object> beans = context.getCodemaBeanFactory().getBeans(Object.class,
                codemaBean -> codemaBean.getConfig() != null && config.getFromConfig()==codemaBean.getConfig());
            return beans;
        }
        if (config.isFromSource()) {
            return Lists.newArrayList(context.getSource());
        }
        return Lists.newLinkedList();
    }
    private <A> Class<A> getType(int i) {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<A>) parameterizedType.getActualTypeArguments()[i];
    }
    @Override public Class<T> getConfigType() {
        return getType(0);
    }

    @Override public Class getSourceType() {
        return getType(1);
    }

    @Override public Class getDestType() {
        return getType(2);
    }
}