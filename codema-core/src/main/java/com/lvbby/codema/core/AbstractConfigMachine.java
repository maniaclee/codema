package com.lvbby.codema.core;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.config.ConfigProperty;
import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.core.utils.FileUtils;
import com.lvbby.codema.core.utils.ReflectionUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 最基础的抽象类，实现配置信息
 * @author dushang.lp
 * @version $Id: AbstractCodemaMachine.java, v 0.1 2017-08-24 3:32 dushang.lp Exp $
 */
public abstract class AbstractConfigMachine<S, O> implements Machine<S, O> {
    static Logger logger = LoggerFactory.getLogger(AbstractConfigMachine.class);

    @Getter
    @Setter
    protected Result<O>           result;
    @Getter
    protected List<Machine>       machines;
    @ConfigProperty
    protected S                   source;
    @Getter
    protected List<ResultHandler> handlers;
    @ConfigProperty
    @Getter
    @Setter
    protected String              destRootDir;

    protected AbstractConfigMachine parent;


    @Override
    public Machine<S, O> source(S source) {
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
    @Override
    public Machine<S, O> next(Machine next) {
        Validate.notNull(next, "can't be null");
        if (machines == null) {
            machines = Lists.newLinkedList();
        }
        machines.add(next);
        //如果子machine的handlers为空，使用父machine的handler
        if(next instanceof AbstractConfigMachine){
            AbstractConfigMachine nextMachine = (AbstractConfigMachine) next;
            nextMachine.parent = this;
        }
        return this;
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


    @Override public Machine<S, O> resultHandlers(List<ResultHandler> handlers) {
        this.handlers=handlers;
        return this;
    }

    @Override public Machine<S, O> addResultHandler(ResultHandler handler) {
        if(handlers==null){
            handlers=Lists.newLinkedList();
        }
        this.handlers.add(handler);
        return this;
    }


    private <A> Class<A> getType(int i) {
        ParameterizedType parameterizedType = (ParameterizedType) getClass()
                .getGenericSuperclass();
        return (Class<A>) parameterizedType.getActualTypeArguments()[i];
    }

    @Override public Class<S> sourceType() {
        return (Class<S>)getType(0);
    }

    @Override public Class<O> outputType() {
        return (Class<O>) getType(1);
    }


    public AbstractConfigMachine<S, O> destRootDir(String destRootDir) {
        this.destRootDir = destRootDir;
        return this;
    }

    protected String loadResourceAsString(String resourceName)   {
        try {
            return ReflectionUtils.loadResource(getClass(), resourceName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}