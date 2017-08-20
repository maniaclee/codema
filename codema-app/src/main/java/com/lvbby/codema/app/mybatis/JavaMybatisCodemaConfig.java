package com.lvbby.codema.app.mybatis;

import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;

/**
 * Created by lipeng on 2017/1/8.
 */
@ConfigBind(JavaMybatisCodemaMachine.class)
public class JavaMybatisCodemaConfig extends JavaBasicCodemaConfig {
    private String mapperDir;
    private String configPackage;

    public String getConfigPackage() {
        return configPackage;
    }

    public void setConfigPackage(String configPackage) {
        this.configPackage = configPackage;
    }

    public String getMapperDir() {
        return mapperDir;
    }

    public void setMapperDir(String mapperDir) {
        this.mapperDir = mapperDir;
    }
}
