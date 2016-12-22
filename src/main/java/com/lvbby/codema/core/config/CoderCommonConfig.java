package com.lvbby.codema.core.config;

import com.lvbby.codema.core.ConfigKey;

import java.io.Serializable;

/**
 * Created by lipeng on 2016/12/22.
 */
@ConfigKey("common")
public class CoderCommonConfig implements Serializable {
    private String author;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
