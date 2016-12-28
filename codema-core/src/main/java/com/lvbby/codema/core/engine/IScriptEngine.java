package com.lvbby.codema.core.engine;

/**
 * Created by lipeng on 16/12/28.
 */
public interface IScriptEngine {

    String eval(String src, Object parameter) throws Exception;

    String getSupportedScheme();
}
