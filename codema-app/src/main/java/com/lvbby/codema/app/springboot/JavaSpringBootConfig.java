package com.lvbby.codema.app.springboot;

import com.lvbby.codema.core.config.ConfigKey;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;

/**
 * Created by lipeng on 2017/1/3.
 */
@ConfigKey("java.springBoot")
public class JavaSpringBootConfig extends JavaBasicCodemaConfig {
    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
