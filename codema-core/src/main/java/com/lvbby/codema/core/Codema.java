package com.lvbby.codema.core;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.config.NotBlank;
import com.lvbby.codema.core.source.SourceLoader;
import com.lvbby.codema.core.source.SourceLoaderCallback;
import com.lvbby.codema.core.utils.ReflectionUtils;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by lipeng on 16/12/23.
 */
public class Codema {
    /***
     * 根据source分批的任务
     */
    private List<CodemaJob> jobs = Lists.newLinkedList();

    public static <T extends CommonCodemaConfig> void exec(T config, SourceLoader sourceLoader)
            throws Exception {
        sourceLoader(sourceLoader).bind(config).run();
    }

    public static Codema source(Object source) {
        Codema codema = new Codema();
        if (source instanceof List) {
            for (Object s : ((List) source)) {
                codema.jobs.add(new CodemaJob(s));
            }
        } else {
            codema.jobs.add(new CodemaJob(source));
        }
        return codema;
    }

    public static Codema sourceLoader(SourceLoader sourceLoader) throws Exception {
        List source = sourceLoader.loadSource();
        Codema codema = new Codema();
        for (Object o : source) {
            CodemaJob e = new CodemaJob(o);
            //处理回调
            if(sourceLoader instanceof SourceLoaderCallback){
                ((SourceLoaderCallback) sourceLoader).process(o,e.codemaContext);
            }
            codema.jobs.add(e);
        }
        return codema;
    }

    /***
     *
     * @param configBinder
     * @param config
     * @param <T>
     * @return
     */
    public <T extends CommonCodemaConfig> Codema bind(CodemaMachine<T> configBinder, T config) {
        for (CodemaJob job : jobs) {
            job.codemaContext.getRunMap().put(configBinder, config);
        }
        return this;
    }

    public <T extends CommonCodemaConfig> Codema bind(T config) {
        for (CodemaJob job : jobs) {
            CodemaMachine instance = config.loadCodemaMachine();
            Validate.notNull(instance, "no codema machine found for config %s",
                    config.getClass().getSimpleName());
            job.codemaContext.getRunMap().put(instance, config);
        }
        return this;
    }

    /***
     * 根据source，分批执行
     * @throws Exception
     */
    public void run() throws Exception {
        for (CodemaJob job : jobs) {
            job.run();
        }
    }

    /***
     * 并发执行，注意异常
     * @param executor
     */
    public void runParallel(Executor executor) {
        for (CodemaJob job : jobs) {
            executor.execute(() -> {
                try {
                    job.run();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

    }

    private static class CodemaJob {
        /**每个job持有一个context，为了线程安全和ThreadLocal*/
        private CodemaContext codemaContext = new CodemaContext();

        public CodemaJob(Object source) {
            codemaContext.setSource(source);
        }

        public CodemaContext getCodemaContext() {
            return codemaContext;
        }

        public void run() throws Exception {
            CodemaContextHolder.setCodemaContext(codemaContext);
            try {
                Validate.notNull(codemaContext.getSource(), "no source found");
                checkConfig();
                /** 执行 */
                for (CodemaMachine codemaMachine : codemaContext.getRunMap().keySet()) {
                    for (CommonCodemaConfig config : codemaContext.getRunMap().get(codemaMachine)) {
                        //初始化config
                        config.init();
                        try {
                            codemaMachine.code(codemaContext, config);
                        } catch (Error e) {
                            throw new RuntimeException(String.format("error machine[%s], error[%s]",
                                    codemaMachine.getClass().getName(), e.getMessage()), e);
                        }
                    }
                }
            } finally {
                CodemaContextHolder.clear();
            }
        }

        private void checkConfig() throws Exception {
            for (CommonCodemaConfig commonCodemaConfig : codemaContext.getRunMap().values()) {
                //NotBlank
                for (Field field : ReflectionUtils.getAllFields(commonCodemaConfig.getClass(),
                        field -> field.isAnnotationPresent(NotBlank.class))) {
                    field.setAccessible(true);
                    Object o = field.get(commonCodemaConfig);
                    if (o instanceof String) {
                        Validate.notBlank(o.toString(), "%s.%s can't be blank");
                    }
                }
            }
        }
    }

}
