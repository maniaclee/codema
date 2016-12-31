package com.lvbby.codema.core.config;

import com.lvbby.codema.core.ConfigKey;
import com.lvbby.codema.core.ResultHandler;

import java.io.Serializable;

/**
 * Created by lipeng on 2016/12/22.
 */
@ConfigKey("common")
public class CommonCodemaConfig implements Serializable {
    private String author;
    private String from;
    private String resultHandler;

    public String getResultHandler() {
        return resultHandler;
    }

    public void setResultHandler(String resultHandler) {
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

    public ResultHandler findResultHandler() {
        return findResultHandler(getResultHandler());
    }

    public static ResultHandler findResultHandler(String handler) {
        try {
            Object o = Class.forName(handler).newInstance();
            if (o instanceof ResultHandler)
                return (ResultHandler) o;
        } catch (Exception e) {
        }
        throw new IllegalArgumentException("handler not found");
    }
}
