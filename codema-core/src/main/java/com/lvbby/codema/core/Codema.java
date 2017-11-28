package com.lvbby.codema.core;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.config.NotBlank;
import com.lvbby.codema.core.source.SourceLoader;
import com.lvbby.codema.core.source.SourceLoaderCallback;
import com.lvbby.codema.core.utils.ReflectionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by lipeng on 16/12/23.
 */
public class Codema<S> {

    private List<S> sources=Lists.newLinkedList();
    private List<CodemaMachine> machines = Lists.newLinkedList();

    public static <T extends CommonCodemaConfig> void exec(T config, SourceLoader sourceLoader)
            throws Exception {
        sourceLoader(sourceLoader).bind(config).run();
    }

    public static <S> Codema<S> instance() {
        return source(null);
    }
    public static <S>Codema<S> source(S source) {
        Codema<S> codema = new Codema();
        //source可以为空
        if(source!=null) {
            codema.sources.add(source);
        }
        return codema;
    }
    public static <S>Codema<S> source(List<S> source) {
        Codema<S> codema = new Codema();
        //source可以为空
        if(CollectionUtils.isNotEmpty(source)) {
            codema.sources.addAll(source);
        }
        return codema;
    }

    public static <S>Codema<S> sourceLoader(SourceLoader<S> sourceLoader) throws Exception {
        if(sourceLoader==null){
            return instance();
        }
        return source(sourceLoader.loadSource());
    }

    /***
     *
     * @param machine
     * @param <T>
     * @return
     */
    public <T extends CommonCodemaConfig,O> Codema machine(CodemaMachine<T,S,O> machine) {
        return machine(machine,null);
    }
    public <T extends CommonCodemaConfig,O> Codema machine(CodemaMachine<T,S,O> machine, T config) {
        if(machine!=null){
            if(config!=null){
                machine.setConfig(config);
            }
            machines.add(machine);
        }
        return this;
    }

    public <T extends CommonCodemaConfig> Codema bind(T config) {
        CodemaMachine instance = config.loadCodemaMachine();
        Validate.notNull(instance, "no codema machine found for config %s",
                config.getClass().getSimpleName());
        machines.add(instance.setConfig(config));
        return this;
    }

    /***
     * 根据source，分批执行
     * @throws Exception
     */
    public void run() throws Exception {
        for (S source : sources) {
            for (CodemaMachine machine : machines) {
                machine.code(source);
            }
        }
    }

    /***
     * 并发执行，注意异常
     * @param executor
     */
    public void runParallel(Executor executor) {
        for (S source : sources) {
            for (CodemaMachine machine : machines) {
                executor.execute(() -> {
                    try {
                        machine.code(source);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }
}
