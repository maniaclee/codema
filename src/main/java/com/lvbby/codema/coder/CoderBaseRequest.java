package com.lvbby.codema.coder;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by lipeng on 2016/12/20.
 * coder request
 */
public class CoderBaseRequest {
    private String author;
    private Object argOrigin;
    private Map<String, Object> params = Maps.newHashMap();

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Object getArgOrigin() {
        return argOrigin;
    }

    public void setArgOrigin(Object argOrigin) {
        this.argOrigin = argOrigin;
    }

}
