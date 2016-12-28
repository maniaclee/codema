package com.lvbby.codema.core.engine;

/**
 * Created by lipeng on 16/12/28.
 */
public class JsEngine extends BaseEngine {

    public JsEngine() {
        setEngine("JavaScript");
    }

    @Override
    public String getSupportedScheme() {
        return "script://js/";
    }
}
