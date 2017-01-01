package com.lvbby.codema.core.config;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.ResultHandler;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2016/12/22.
 */
@ConfigKey("common")
public class CommonCodemaConfig implements Serializable, ResultHandler {
    private String author;
    private String from;
    private List<String> resultHandler;

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

    public List<ResultHandler> findResultHandler() {
        LinkedList<ResultHandler> re = Lists.newLinkedList();
        if (resultHandler == null) {
            return re;
        }
        return resultHandler.stream().map(e -> findResultHandler(e)).collect(Collectors.toList());
    }

    public static ResultHandler findResultHandler(String handler) {
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
    public void handle(CodemaContext codemaContext, Object result) {
        for (ResultHandler handler : findResultHandler()) {
            handler.handle(codemaContext, result);
        }
    }
}
