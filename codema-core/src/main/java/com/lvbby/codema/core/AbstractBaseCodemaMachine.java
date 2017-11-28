package com.lvbby.codema.core;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.core.utils.ReflectionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * 最基础的抽象类，实现基本功能和一些模板方法
 * @author dushang.lp
 * @version $Id: AbstractCodemaMachine.java, v 0.1 2017-08-24 3:32 dushang.lp Exp $
 */
public abstract class AbstractBaseCodemaMachine<T extends CommonCodemaConfig,S,O>
                                           implements CodemaMachine<T,S,O> {
    private Result<O>           result;
    private T                   config;
    private List<CodemaMachine> machines;

    @Override public  CodemaMachine<T , S, O> next(CodemaMachine next) {
        Validate.notNull(next,"can't be null");
        if(machines == null){
            machines =Lists.newLinkedList();
        }
        //check input type & output type
        Validate.isTrue(next.getSourceType().isAssignableFrom(getDestType()),
                "%s's output type[%s] doesn't match %s's input[%s]",
                getClass().getSimpleName(),
                getDestType().getSimpleName(),
                next.getClass().getSimpleName(),
                next.getSourceType().getSimpleName()
                );
        machines.add(next);
        return this;
    }

    @Override public <ConfigOther extends CommonCodemaConfig, Output> CodemaMachine<T, S, O> nextWithCheck(
            CodemaMachine<ConfigOther, O, Output> next) {
         machines.add(next);
         return this;
    }

    @Override public Result<O> getResult() {
        return result;
    }

    @Override
    public <T> T getArg(TypedKey<T> key) {
        return map.get(key);
    }

    protected void setResult(Result<O> result){
        this.result=result;
    }

    protected String loadResourceAsString(String resourceName) throws IOException {
        return ReflectionUtils.loadResource(getClass(), resourceName);
    }

    @Override public void code(S source) throws Exception {
        code(getConfig(),source);
        //触发后续的machine
        if(CollectionUtils.isNotEmpty(machines) && getResult() != null && getResult().getResult() != null){
            for (CodemaMachine codemaMachine : machines) {
                codemaMachine.code(getResult());
            }
        }
    }

    protected abstract void code(T config, S source) throws Exception;

    @Override
    public T getConfig() {
        return config;
    }
    @Override
    public CodemaMachine<T , S, O> setConfig(T config) {
        this.config = config;
        return this;
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