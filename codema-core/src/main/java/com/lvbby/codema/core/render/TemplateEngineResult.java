package com.lvbby.codema.core.render;

import com.alibaba.fastjson.JSON;
import com.lvbby.codema.core.error.CodemaRuntimeException;
import com.lvbby.codema.core.result.FileResult;
import com.lvbby.codema.core.result.PrintableResult;
import com.lvbby.codema.core.utils.ReflectionUtils;

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

    public static TemplateEngineResult of(String template, Object arg, File file) {
        TemplateEngineResult re = new TemplateEngineResult();
        re.template = template;
        re.arg = arg;
        re.file = file;
        return re;
    }

    @Override
    public Object getResult() {
        return arg;
    }

    @Override
    public String getString() {
        if (result != null)
            return result;
        if (!(arg instanceof Map)) {
            try {
                arg = ReflectionUtils.object2map(arg);
            } catch (Exception e) {
                e.printStackTrace();
                throw new CodemaRuntimeException("failed to convert arg to map: " + JSON.toJSONString(arg), e);
            }
        }
        Map map = (Map) arg;
        TemplateEngine templateEngine = TemplateEngineFactory.create(template);
        map.keySet().forEach(o -> templateEngine.bind(o.toString(), map.get(o)));
        result = templateEngine.render();
        return result;
    }

    @Override
    public File getFile() {
        return file;
    }
}
