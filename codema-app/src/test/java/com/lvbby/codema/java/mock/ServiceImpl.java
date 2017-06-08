package com.lvbby.codema.java.mock;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by dushang.lp on 2017/5/31.
 */
public class ServiceImpl implements IService {
    @Autowired
    private TextHolder textHolder;

    @Override
    public String echo(String s) {
        return String.format("echo ---->   [%s.%s]  --->  %s ", textHolder.getClass().getName(), textHolder.getText(), s);
    }

    public TextHolder getTextHolder() {
        return textHolder;
    }

    public void setTextHolder(TextHolder textHolder) {
        this.textHolder = textHolder;
    }
}