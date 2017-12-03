package com.lvbby.codema.core;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.source.SourceLoader;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by lipeng on 16/12/23.
 */
public class Codema<S> {

    private List<S>       sources  = Lists.newLinkedList();
    private List<Machine> machines = Lists.newLinkedList();

    public <O> Codema<S> addMachine(Machine<S,O> machine){
        machines.add(machine);
        return this;
    }

    public static void exec(SourceLoader sourceLoader) throws Exception {
        sourceLoader(sourceLoader).run();
    }

    public static <S> Codema<S> instance() {
        return source(null);
    }

    public static <S> Codema<S> source(S source) {
        Codema<S> codema = new Codema();
        //source可以为空
        if (source != null) {
            codema.sources.add(source);
        }
        return codema;
    }

    public static <S> Codema<S> source(List<S> source) {
        Codema<S> codema = new Codema();
        //source可以为空
        if (CollectionUtils.isNotEmpty(source)) {
            codema.sources.addAll(source);
        }
        return codema;
    }

    public static <S> Codema<S> sourceLoader(SourceLoader<S> sourceLoader) throws Exception {
        if (sourceLoader == null) {
            return instance();
        }
        return source(sourceLoader.loadSource());
    }

    /***
     * 根据source，分批执行
     * @throws Exception
     */
    public void run() throws Exception {
        try {
            for (S source : sources) {
                for (Machine machine : machines) {
                    machine.source(source);
                    machine.code();
                }
            }
        } finally {
            CodemaContextHolder.clear();
        }
    }

    /***
     * 并发执行，注意异常
     * @param executor
     */
    public void runParallel(Executor executor) {
        for (S source : sources) {
            for (Machine machine : machines) {
                executor.execute(() -> {
                    try {
                        machine.source(source);
                        machine.code();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }
}
