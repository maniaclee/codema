package com.lvbby.codema.core.engine;

import com.lvbby.codema.core.error.CodemaException;
import com.lvbby.codema.core.utils.CodemaUtils;

import java.net.URI;

/**
 * Created by lipeng on 17/1/10.
 */
public class FunctionEngine implements IScriptEngine {
    @Override
    public String eval(URI src, Object parameter) throws Exception {
        String v = parameter.toString();
        String args = CodemaUtils.getPathPart(src);
        String schemeSpecificPart = src.getSchemeSpecificPart();
        switch (schemeSpecificPart) {
            case "suffix":
                return v + args;
        }
        throw new CodemaException("unsupported function:" + schemeSpecificPart);
    }

    @Override
    public String getSupportedScheme() {
        return "script://function:";
    }
}
