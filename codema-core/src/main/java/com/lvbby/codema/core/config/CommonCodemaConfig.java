package com.lvbby.codema.core.config;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.ResultContext;
import com.lvbby.codema.core.ResultHandler;
import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.core.utils.ReflectionUtils;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lipeng on 2016/12/22.
 */
@ConfigKey("common")
public class CommonCodemaConfig implements Serializable, ResultHandler {
    private String author = System.getProperty("user.name");
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


    @Override
    public void handle(ResultContext resultContext) throws Exception {
        for (ResultHandler handler : resultHandlers) {
            handler.handle(resultContext);
        }
    }

    public void handle(CodemaContext codemaContext, Result result) throws Exception {
        handle(ResultContext.of(codemaContext, this, result));
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
