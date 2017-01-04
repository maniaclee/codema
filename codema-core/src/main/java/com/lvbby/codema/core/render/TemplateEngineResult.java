package com.lvbby.codema.core.render;

import com.lvbby.codema.core.result.FileResult;
import com.lvbby.codema.core.result.PrintableResult;

import java.io.File;

/**
 * Created by lipeng on 2017/1/3.
 */
public class TemplateEngineResult implements PrintableResult, FileResult {
    private String template;
    private Object arg;
    private String result;

    public TemplateEngineResult(String template, Object arg) {
        this.template = template;
        this.arg = arg;
        TemplateEngine templateEngine = TemplateEngineFactory.create(template);
        result = templateEngine.render();
    }

    @Override
    public Object getResult() {
        return arg;
    }

    @Override
    public String getString() {
        return result;
    }

    @Override
    public File getFile() {
        return null;
    }
}
