package com.lvbby.codema.core.engine;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaException;

import java.net.URI;
import java.util.List;

/**
 * Created by lipeng on 16/12/28.
 */
public class ScriptEngineFactory {

    public static final ScriptEngineFactory instance = new ScriptEngineFactory(Lists.newArrayList(new JsEngine()));
    private List<IScriptEngine> scriptEngines = Lists.newArrayList();

    public static ScriptEngineFactory of(List<IScriptEngine> scriptEngines) {
        return new ScriptEngineFactory(scriptEngines);
    }

    private ScriptEngineFactory(List<IScriptEngine> scriptEngines) {
        if (scriptEngines != null)
            this.scriptEngines = scriptEngines;
    }

    public String eval(String uri, Object parameter) throws Exception {
        return eval(URI.create(uri), parameter);
    }

    public String eval(URI uri, Object parameter) throws Exception {
        return scriptEngines.stream().filter(iScriptEngine -> uri.toString().startsWith(iScriptEngine.getSupportedScheme())).findFirst()
                .orElseThrow(() -> new CodemaException("no script engine found for" + uri.toString()))
                .eval(uri.getPath(), parameter);
    }
}
