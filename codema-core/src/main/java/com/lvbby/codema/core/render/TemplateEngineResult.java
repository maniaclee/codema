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
        return of(TemplateEngineResult.class,template,arg,file);
    }

    public static <T extends TemplateEngineResult> T of(Class<T> t, String template, Object arg, File file) {
        T re = null;
        try {
            re = t.newInstance();
        } catch (Exception e) {
            throw new CodemaRuntimeException("error create template result for class : " + t.getName());
        }
        re.setTemplate(template);
        re.setArg(arg);
        re.setFile(file);
        return re;
    }

    protected String processTemplate(String template, Object arg){
        return template;
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

    protected void setTemplate(String template) {
        this.template = template;
    }

    protected void setArg(Object arg) {
        this.arg = arg;
    }

    protected void setFile(File file) {
        this.file = file;
    }
}
