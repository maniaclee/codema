package com.lvbby.codema.core.engine;

import com.lvbby.codema.core.CodemaException;
import com.lvbby.codema.core.utils.JavaUtils;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Map;

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
        Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        if (parameter == null || parameter.getClass().isPrimitive())
            throw new CodemaException("parameter can't be null or primitive");
        if (parameter instanceof Map) {
            bindings.putAll((Map<? extends String, ? extends Object>) parameter);
        } else {
            bindings.putAll(JavaUtils.object2map(parameter));
        }
        return engine.eval(src, bindings).toString();
    }

}
