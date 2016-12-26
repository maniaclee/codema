package com.lvbby.codema.core.config;

import com.lvbby.codema.core.ConfigKey;
import com.lvbby.codema.core.ResultHandler;

import java.io.Serializable;

/**
 * Created by lipeng on 2016/12/22.
 */
@ConfigKey("common")
public class CoderCommonConfig implements Serializable {
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
        return null;//TODO
    }

    public static ResultHandler findResultHandler(String handler) {
        return null;//TODO
    }
}
