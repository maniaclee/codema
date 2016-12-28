package com.lvbby.codema.core.engine;

import javax.script.*;

/**
 * Created by lipeng on 16/12/28.
 */
public class JsEngine implements IScriptEngine {
    private static ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

    @Override
    public String eval(String src, Object parameter) throws Exception {
        Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        bindings.put("$src", parameter);
        return engine.eval(src, bindings).toString();
    }

    @Override
    public String getSupportedScheme() {
        return "script://js/";
    }
}
