package com.lvbby.codema.executor;

import com.lvbby.codema.core.bean.BaseEntity;

/**
 * Created by lipeng on 2018/3/22.
 */
public class MachineRequestArg extends BaseEntity {
    private static final long serialVersionUID = 4118611566443489535L;
    private String key;
    private String value;
    private String type;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
