package com.lvbby.codema.bean;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lipeng on 16/8/3.
 */
public class JavaBean {
    String className;
    String pack;
    List<JavaBeanField> fields = new LinkedList<>();

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public List<JavaBeanField> getFields() {
        return fields;
    }

    public void setFields(List<JavaBeanField> fields) {
        this.fields = fields;
    }
}
