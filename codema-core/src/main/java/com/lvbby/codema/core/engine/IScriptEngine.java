package com.lvbby.codema.core.engine;

import java.net.URI;

/**
 * Created by lipeng on 16/12/28.
 */
public interface IScriptEngine {

    String eval(URI src, Object parameter) throws Exception;

    String getSupportedScheme();
}
