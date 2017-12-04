package com.lvbby.codema.core;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.config.ConfigProperty;
import com.lvbby.codema.core.config.NotBlank;
import com.lvbby.codema.core.config.NotNull;
import com.lvbby.codema.core.handler.ResultHandlerFactory;
import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.core.utils.FileUtils;
import com.lvbby.codema.core.utils.ReflectionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 最基础的抽象类，实现基本功能和一些模板方法
 * @author dushang.lp
 * @version $Id: AbstractCodemaMachine.java, v 0.1 2017-08-24 3:32 dushang.lp Exp $
 */
public abstract class AbstractBaseMachine<S, O> implements Machine<S, O> {
    static Logger logger = LoggerFactory.getLogger(AbstractBaseMachine.class);
    protected Result<O>           result;
    protected List<Machine>       machines;
    protected AbstractBaseMachine parent;
    @ConfigProperty
    protected S                   source;
    protected List<ResultHandler> handlers=Lists.newArrayList(ResultHandlerFactory.print);
    @ConfigProperty
    protected String              destRootDir;
    private   Machine             dependency;
    private   Supplier            param;

    @Override public Machine<S, O> source(S source) {
        this.source = source;
        return this;
    }

    /***
     * 基于当前的路径构建路径
     * @param subDir
     * @return
     */
    public String buildDir(String subDir){
        Validate.notBlank(destRootDir,"destRootDir can't be empty");
        return new File(destRootDir,subDir).getAbsolutePath();
    }
    @Override public S getSource() {
        return source;
    }

    @Override public void code() throws Exception {
        check();
        doCode();
        //触发后续的machine
        if (CollectionUtils.isNotEmpty(machines) && getResult() != null
            && getResult().getResult() != null) {
            for (Machine machine : machines) {
                //设置source
                machine.source(getResult().getResult());
                //run
                machine.code();
            }
        }
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
            for (AbstractBaseMachine p = parent; p != null; p = p.parent) {
                if (CollectionUtils.isNotEmpty(p.handlers)) {
                    hs = p.handlers;
                    break;
                }
            }
        }
        if (CollectionUtils.isNotEmpty(hs)) {
            for (ResultHandler handler : hs) {
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

    protected <T>  T findBeanAny(Class<T> clz , Predicate<T> predicate){
        return CodemaContextHolder.get().getCodemaBeanFactory().getBeans(clz).stream().filter(predicate).findAny().orElse(null);
    }
    protected <T>  List<T> findBean(Class<T> clz , Predicate<T> predicate){
        return CodemaContextHolder.get().getCodemaBeanFactory().getBeans(clz).stream().filter(predicate).collect(
                Collectors.toList());
    }
    protected String parseFileWithParent(String parent, String sub, String message) {
        return parseFileWithParent(parent, sub, message, false);
    }

    /***
     * 根据parent解析file
     * 1. parent 必须存在,如果不存在返回null
     * 2. 如果sub是绝对路径，返回sub
     * @param parent
     * @param sub
     * @param message
     * @return
     */
    protected String parseFileWithParent(String parent, String sub, String message, boolean parentMustExist) {
        if (StringUtils.isBlank(parent)) {
            if (StringUtils.isNotBlank(sub)) {
                logger.warn("{}parent is null,ignore path {}", message, sub);
            }
            return null;
        }
        if (StringUtils.isBlank(sub)) {
            logger.warn("{} ignore blank path", message);
            return null;
        }
        Validate.notBlank(sub, "[%s]child file can't be empty,parent dir: %s", message, sub);
        if (parentMustExist) {
            Validate.isTrue(
                    FileUtils.exist(parent), "[%s]parent file not exist:%s".format(message, parent));
        }
        //解析子路径
        String subFile = FileUtils.parseFileRoot(sub).getPath();
        if (!FileUtils.isRelativeFilePath(subFile)) {
            Validate.isTrue(FileUtils.exist(subFile), "[%s]path not exist: %s", message, sub);
            return subFile;
        }
        return FileUtils.parseFile(parent, subFile).getAbsolutePath();
    }


    @Override public Machine<S, O> next(Machine next) {
        Validate.notNull(next, "can't be null");
        if (machines == null) {
            machines = Lists.newLinkedList();
        }
        //check input type & output type
//        Validate.isTrue(next.sourceType().isAssignableFrom(outputType()),
//                "%s's output type[%s] doesn't match %s's input[%s]", getClass().getSimpleName(),
//                outputType().getSimpleName(), next.getClass().getSimpleName(),
//                next.sourceType().getSimpleName());
        machines.add(next);
        //如果子machine的handlers为空，使用父machine的handler
        if(next instanceof AbstractBaseMachine){
            AbstractBaseMachine nextMachine = (AbstractBaseMachine) next;
            nextMachine.parent = this;
        }
        return this;
    }

    @Override public <Output> Machine<S, O> nextWithCheck(Machine<O, Output> next) {
        next(next);
        return this;
    }

    @Override public Machine<S, O> resultHandlers(List<ResultHandler> handlers) {
        this.handlers=handlers;
        return this;
    }

    @Override public Machine<S, O> depend(Machine machine) {
        this.dependency = machine;
        return this;
    }

    @Override public <T> T getDependency() {
        if(dependency!=null&& dependency.getResult()!=null){
            return (T) dependency.getResult().getResult();
        }
        return null;
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
        ParameterizedType parameterizedType = (ParameterizedType) getClass()
                .getGenericSuperclass();
        return (Class<A>) parameterizedType.getActualTypeArguments()[i];
    }

    @Override public Class<S> sourceType() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass()
                .getGenericSuperclass();
        return (Class<S>) parameterizedType.getActualTypeArguments()[0];
    }

    @Override public Class<O> outputType() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass()
                .getGenericSuperclass();
        return (Class<O>) parameterizedType.getActualTypeArguments()[1];
    }

    public String getDestRootDir() {
        return destRootDir;
    }

    public void setDestRootDir(String destRootDir) {
        this.destRootDir = destRootDir;
    }

    public AbstractBaseMachine<S, O> destRootDir(String destRootDir) {
        this.destRootDir = destRootDir;
        return this;
    }
}