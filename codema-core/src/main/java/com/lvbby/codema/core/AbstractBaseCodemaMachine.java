package com.lvbby.codema.core;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.config.ConfigProperty;
import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.core.source.SourceLoader;
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
public abstract class AbstractBaseCodemaMachine<S, O> implements CodemaMachine<S, O> {
    protected Result<O>           result;
    protected List<CodemaMachine> machines;
    protected S                   source;
    protected List<ResultHandler> handlers;
    @ConfigProperty
    protected String              destRootDir;

    @Override public CodemaMachine<S, O> source(S source) {
        this.source = source;
        return this;
    }

    @Override public void code() throws Exception {
        doCode();
        //触发后续的machine
        if (CollectionUtils.isNotEmpty(machines) && getResult() != null
            && getResult().getResult() != null) {
            for (CodemaMachine codemaMachine : machines) {
                //设置source
                codemaMachine.source(getResult());
                //run
                codemaMachine.code();
            }
        }
    }

    protected abstract void doCode() throws Exception;

    /***
     * 处理result，并设置result
     * 子类都需要调用这个方法来处理result
     * @param result
     * @throws Exception
     */
    protected void handle(Result<O> result) throws Exception {
        setResult(result);
        if (CollectionUtils.isNotEmpty(handlers)) {
            for (ResultHandler handler : handlers) {
                handler.handle(result);
            }
        }
    }

    /***
     * 处理result，但是不设置result
     * @param result
     * @throws Exception
     */
    protected void handleSimple(Result<O> result) throws Exception {
        if (CollectionUtils.isNotEmpty(handlers)) {
            for (ResultHandler handler : handlers) {
                handler.handle(result);
            }
        }
    }

    @Override public CodemaMachine<S, O> next(CodemaMachine next) {
        Validate.notNull(next, "can't be null");
        if (machines == null) {
            machines = Lists.newLinkedList();
        }
        //check input type & output type
        Validate.isTrue(next.sourceType().isAssignableFrom(outputType()),
                "%s's output type[%s] doesn't match %s's input[%s]", getClass().getSimpleName(),
                outputType().getSimpleName(), next.getClass().getSimpleName(),
                next.sourceType().getSimpleName());
        machines.add(next);
        return this;
    }

    @Override public <Output> CodemaMachine<S, O> nextWithCheck(CodemaMachine<O, Output> next) {
        machines.add(next);
        return this;
    }

    @Override public CodemaMachine<S, O> resultHandlers(List<ResultHandler> handlers) {
        this.handlers=handlers;
        return this;
    }

    @Override public Result<O> getResult() {
        return result;
    }

    protected void setResult(Result<O> result) {
        this.result = result;
    }

    protected String loadResourceAsString(String resourceName) throws IOException {
        return ReflectionUtils.loadResource(getClass(), resourceName);
    }

    private <A> Class<A> getType(int i) {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass()
                .getGenericSuperclass();
        return (Class<A>) parameterizedType.getActualTypeArguments()[i];
    }

    @Override public Class sourceType() {
        return getType(0);
    }

    @Override public Class outputType() {
        return getType(1);
    }

    public String getDestRootDir() {
        return destRootDir;
    }

    public void setDestRootDir(String destRootDir) {
        this.destRootDir = destRootDir;
    }
}