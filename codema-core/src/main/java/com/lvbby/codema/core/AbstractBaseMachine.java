package com.lvbby.codema.core;

import com.lvbby.codema.core.config.NotBlank;
import com.lvbby.codema.core.config.NotNull;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.core.utils.ReflectionUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

/**
 * 实现machine的基础功能
 * @author dushang.lp
 * @version $Id: AbstractCodemaMachine.java, v 0.1 2017-08-24 3:32 dushang.lp Exp $
 */
public abstract class AbstractBaseMachine<S, O> extends AbstractConfigMachine<S, O> {

    @Override
    public void run() throws Exception {
        try {
            /** 标识当前的machine */
            CodemaContextHolder.get().setCurrentMachine(this);
            check();
            doCode();
            //触发后续的machine
            if (getResult() != null && getResult().getResult() != null) {
                invokeNext(getResult().getResult());
            }
        } finally {
            CodemaContextHolder.clear();
        }
    }

    /***
     * 处理批量任务
     * @param resultList
     * @throws Exception
     */
    protected void handleList(List<O> resultList) throws Exception {
        handleList(resultList,null);
    }
    /***
     * 处理result，并设置result
     * 子类都需要调用这个方法来处理result
     * @param result
     * @throws Exception
     */
    protected void handle(Result result) throws Exception {
        setResult(result);
        List<ResultHandler> hs = handlers;
        //如果handlers为空，一直找父亲节点的handlers
        if (CollectionUtils.isEmpty(hs)) {
            for (AbstractConfigMachine p = parent; p != null; p = p.parent) {
                if (CollectionUtils.isNotEmpty(p.handlers)) {
                    hs = p.handlers;
                    break;
                }
            }
        }
        invokeResultHandlers(result,hs);
    }

    /***
     * 处理result，但是不设置result
     * @param result
     * @throws Exception
     */
    protected void handleSimple(Result result) throws Exception {
        invokeResultHandlers(result,handlers);
    }

    private void invokeResultHandlers(Result result,List<ResultHandler> handlers) throws Exception {
        if (CollectionUtils.isNotEmpty(handlers)) {
            for (ResultHandler handler : handlers) {
                handler.handle(result);
            }
        }
    }
    /***
     * 处理批量任务
     * @param resultList
     * @param resultBuilder
     * @throws Exception
     */
    protected void handleList(List<O> resultList, Function<O,Result<O>> resultBuilder) throws Exception {
        for (O result : resultList) {
            //调用处理器，但是不设置result
            Result<O> instance = resultBuilder==null?BasicResult.instance(result):resultBuilder.apply(result);
            handleSimple(instance);
            //出发后续流程
            invokeNext(result);
        }
    }
    /***
     * 让后续machine执行
     * @param srcForNext
     * @throws Exception
     */
    private void invokeNext(O srcForNext) throws Exception {
        if (CollectionUtils.isNotEmpty(machines)) {
            for (Machine machine : machines) {
                //设置source
                machine.source(srcForNext);
                //run
                machine.run();
            }
        }
    }

    /**
     * 触发其他的machine
     * @param source
     * @param machine
     * @throws Exception
     */
    public void invokeSub(Object source , AbstractBaseMachine machine ) throws Exception {
        machine.parent=this;
        machine.source(source)
        .resultHandlers(getHandlers())
        .run();
    }

    public <T extends AbstractBaseMachine> T copy(Class<T> clz) throws Exception {
        T t = clz.newInstance();
        BeanUtils.copyProperties(t,this);
        return t;
    }

    private void check() throws Exception{
        for (Field field : ReflectionUtils.getAllFields(getClass(), null)) {
            Object object = field.get(this);
            if(field.isAnnotationPresent(NotNull.class)){
                Validate.notNull(object,"%s can't be null",field.getName());
            }
            if(field.isAnnotationPresent(NotBlank.class)){
                Validate.notNull(object,"%s can't be null",field.getName());
                Validate.isTrue(object instanceof String ,"%s must be String",field.getName());
                Validate.notBlank(object.toString(),"%s can't be blank",field.getName());
            }
        }
    }

    protected abstract void doCode() throws Exception;



}