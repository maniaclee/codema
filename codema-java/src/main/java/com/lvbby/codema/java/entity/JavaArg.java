package com.lvbby.codema.java.entity;

import com.lvbby.codema.java.tool.JavaLexer;

/**
 * Created by lipeng on 17/1/5.
 */
public class JavaArg {
    private String name;
    private String type;

    public String getDefaultValue() {
        return JavaLexer.newInstanceForDefaultValue(type).toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
