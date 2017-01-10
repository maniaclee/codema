package com.lvbby.codema.core.engine;

import com.lvbby.codema.core.error.CodemaException;
import com.lvbby.codema.core.utils.CodemaUtils;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lipeng on 17/1/10.
 */
public class FunctionEngine implements IScriptEngine {
    @Override
    public String eval(URI src, Object parameter) throws Exception {
        String v = parameter.toString();
        String args = CodemaUtils.getPathPart(src);
        String fun = getFunction(src);
        switch (fun) {
            case "suffix":
                return v + args;
        }
        throw new CodemaException("unsupported function:" + fun);
    }

    @Override
    public String getSupportedScheme() {
        return "script://function:";
    }

    private String getFunction(URI src) throws CodemaException {
        for (Matcher m = Pattern.compile("[^:]+:([^/]+)").matcher(src.getAuthority()); m.find(); )
            return m.group(1);
        throw new CodemaException("unsupported function:" + src);
    }

}
