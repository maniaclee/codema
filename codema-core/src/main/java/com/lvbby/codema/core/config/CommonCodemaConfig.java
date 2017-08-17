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
    private List<ResultHandler> resultHandlers = Lists.newLinkedList();

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
        for (ResultHandler handler : resultHandlers) {
            handler.handle(resultContext);
        }
    }

    public void handle(CodemaContext codemaContext, CommonCodemaConfig config, Result result) throws Exception {
        handle(ResultContext.of(codemaContext, config, result));
    }

    public void addResultHandler(Class<? extends ResultHandler> resultHandler) {
        this.resultHandlers.add(ReflectionUtils.instance(resultHandler));
    }

    public List<ResultHandler> getResultHandlers() {
        return resultHandlers;
    }

    public void setResultHandlers(List<ResultHandler> resultHandlers) {
        this.resultHandlers = resultHandlers;
    }
}
