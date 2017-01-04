package com.lvbby.codema.core.render;

import com.alibaba.fastjson.JSON;
import com.lvbby.codema.core.error.CodemaRuntimeException;
import com.lvbby.codema.core.result.FileResult;
import com.lvbby.codema.core.result.PrintableResult;
import com.lvbby.codema.core.utils.JavaUtils;

import java.io.File;
import java.util.Map;

/**
 * Created by lipeng on 2017/1/3.
 */
public class TemplateEngineResult implements PrintableResult, FileResult {
    private String template;
    private Object arg;
    private String result;
    private File file;

    public TemplateEngineResult(String template, Object arg, File file) {
        this.template = template;
        this.arg = arg;
        this.file = file;
        if (!(arg instanceof Map)) {
            try {
                arg = JavaUtils.object2map(arg);
            } catch (Exception e) {
                e.printStackTrace();
                throw new CodemaRuntimeException("failed to convert arg to map: " + JSON.toJSONString(arg), e);
            }
        }
        Map map = (Map) arg;
        TemplateEngine templateEngine = TemplateEngineFactory.create(template);
        map.keySet().forEach(o -> templateEngine.bind(o.toString(), map.get(o)));
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
        return file;
    }
}
