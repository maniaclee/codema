package com.lvbby.codema.app.bean;

import com.lvbby.codema.core.config.ConfigKey;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;

import java.util.List;

/**
 * Created by lipeng on 2017/1/7.
 */
@ConfigKey("java.bean")
public class JavaBeanCodemaConfig extends JavaBasicCodemaConfig {
    /***
     * 可嵌套的Bean 配置
     */
    private List<JavaBeanCodemaConfig> list;

    public List<JavaBeanCodemaConfig> getList() {
        return list;
    }

    public void setList(List<JavaBeanCodemaConfig> list) {
        this.list = list;
    }
}
