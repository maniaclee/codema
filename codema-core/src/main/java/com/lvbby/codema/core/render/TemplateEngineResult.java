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

    public TemplateEngineResult(String template, Object arg) {
        this.template = template;
        this.arg = arg;
    }

    @Override
    public Object getResult() {
        return null;
    }

    @Override
    public String getString() {
        return null;
    }

    @Override
    public File getFile() {
        return null;
    }
}