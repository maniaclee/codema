package com.lvbby.codema.core.engine;

import com.alibaba.fastjson.JSON;
import com.lvbby.codema.core.error.CodemaException;
import com.lvbby.codema.core.utils.JavaUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 16/12/28.
 */
public abstract class BaseEngine implements IScriptEngine {
    private ScriptEngine engine;

    protected void setEngine(String engineName) {
        engine = new ScriptEngineManager().getEngineByName(engineName);
    }

    @Override
    public String eval(String src, Object parameter) throws Exception {
        if (parameter == null || parameter.getClass().isPrimitive())
            throw new CodemaException("parameter can't be null or primitive");
        if (parameter instanceof Map) {
            bind(formatVars((Map<? extends String, ? extends Object>) parameter));
        } else {
            bind(formatVars(JavaUtils.object2map(parameter)));
        }
        Object result = engine.eval(src);
        if (!result.getClass().isPrimitive())
            return JSON.toJSONString(result);
        return result == null ? null : result.toString();
    }

    private void bind(Map<String, Object> map) {
        map.entrySet().forEach(stringObjectEntry -> engine.put(stringObjectEntry.getKey(), stringObjectEntry.getValue()));
    }

    private Map<String, Object> formatVars(Map<? extends String, ? extends Object> map) {
        return map.entrySet().stream().collect(Collectors.toMap(o -> "$" + o.getKey(), o -> o.getValue()));
    }

}
