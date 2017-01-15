package com.lvbby.codema.java.app.mybatis;

import com.lvbby.codema.core.config.ConfigKey;
import com.lvbby.codema.java.app.baisc.JavaBasicCodemaConfig;

/**
 * Created by lipeng on 2017/1/8.
 */
@ConfigKey("java.mybatis")
public class JavaMybatisCodemaConfig extends JavaBasicCodemaConfig {
    private String mapperDir;

    public String getMapperDir() {
        return mapperDir;
    }

    public void setMapperDir(String mapperDir) {
        this.mapperDir = mapperDir;
    }
}
