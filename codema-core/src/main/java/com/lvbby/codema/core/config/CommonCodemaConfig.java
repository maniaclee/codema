package com.lvbby.codema.core.config;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.ResultContext;
import com.lvbby.codema.core.ResultHandler;
import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.core.utils.ReflectionUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2016/12/22.
 */
@ConfigKey("common")
public class CommonCodemaConfig implements Serializable, ResultHandler {
    private String author = System.getProperty("user.name");
    private String from;
    private List<String> resultHandler;
    private String destFile;

    /***
     * 用一个父类当做模板来初始化配置
     * @param srcConfig
     * @param targetConfigClass
     * @param <T>
     * @return
     */
    public static <T extends CommonCodemaConfig> T newConfigFromTemplate(CommonCodemaConfig srcConfig, Class<T> targetConfigClass) {
        T re = ReflectionUtils.instance(targetConfigClass);
        try {
            BeanUtils.copyProperties(re, srcConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return re;
    }

    public List<String> getResultHandler() {
        return resultHandler;
    }

    public void setResultHandler(List<String> resultHandler) {
        this.resultHandler = resultHandler;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDestFile() {
        return destFile;
    }

    public void setDestFile(String destFile) {
        this.destFile = destFile;
    }

    private static ResultHandler instanceResultHandler(String handler) {
        try {
            Object o = Class.forName(handler).newInstance();
            if (o instanceof ResultHandler)
                return (ResultHandler) o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("handler not found");
    }

    @Override
    public void handle(ResultContext resultContext) throws Exception {
        for (ResultHandler handler : findHandlers(resultContext)) {
            handler.handle(resultContext);
        }
    }

    public List<ResultHandler> findHandlers(ResultContext resultContext) {
        return _findHandler(this, resultContext).stream().map(hName -> instanceResultHandler(hName)).collect(Collectors.toList());
    }

    private static List<String> _findHandler(CommonCodemaConfig config, ResultContext resultContext) {
        List<String> re = Lists.newLinkedList();
        if (CollectionUtils.isNotEmpty(config.getResultHandler()))
            return config.getResultHandler();
        Class<?> superclass = config.getClass().getSuperclass();
        while (superclass != null && CommonCodemaConfig.class.isAssignableFrom(superclass)) {
            Class<? extends CommonCodemaConfig> clz = (Class<? extends CommonCodemaConfig>) superclass;
            CommonCodemaConfig superConfig = resultContext.getCodemaContext().getConfig(clz);
            if (superConfig != null) {
                return _findHandler(superConfig, resultContext);
            }
        }
        return re;
    }

    public void handle(CodemaContext codemaContext, CommonCodemaConfig config, Result result) throws Exception {
        handle(ResultContext.of(codemaContext, config, result));
    }

    public void addResultHandler(String resultHandler) {
        Validate.notBlank(resultHandler, "empty resultHandler");
        if (getResultHandler() == null) {
            setResultHandler(Lists.newArrayList());
        }
        this.getResultHandler().add(resultHandler);
    }

    public void addResultHandler(Class resultHandler) {
        this.resultHandler.add(resultHandler.getName());
    }

}
