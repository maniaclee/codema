package com.lvbby.codema.core.engine;

import com.lvbby.codema.core.utils.CodemaUtils;

import java.net.URI;

/**
 * Created by lipeng on 17/1/10.
 */
public class DefaultScriptEngine implements IScriptEngine {
    @Override
    public String eval(URI src, Object parameter) throws Exception {
        return CodemaUtils.getPathPart(src);
    }

    @Override
    public String getSupportedScheme() {
        return "";
    }
}
