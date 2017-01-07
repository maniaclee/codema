package com.lvbby.codema.java.app.source;

import com.lvbby.codema.core.SourceParser;
import com.lvbby.codema.java.app.baisc.JavaSourceParam;

import java.net.URI;

/**
 * Created by lipeng on 2017/1/7.
 */
public class JavaJdbcUrlSourceParser implements SourceParser<JavaSourceParam> {
    @Override
    public String getSupportedUriScheme() {
        return "jdbc://";
    }

    @Override
    public JavaSourceParam parse(URI from) throws Exception {
        return null;//TODO
    }
}
