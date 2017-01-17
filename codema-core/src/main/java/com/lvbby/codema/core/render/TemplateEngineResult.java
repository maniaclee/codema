package com.lvbby.codema.core.render;

import com.google.common.collect.Maps;
import com.lvbby.codema.core.error.CodemaRuntimeException;
import com.lvbby.codema.core.result.FileResult;
import com.lvbby.codema.core.result.PrintableResult;
import com.lvbby.codema.core.utils.ReflectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by lipeng on 2017/1/3.
 */
public class TemplateEngineResult implements PrintableResult, FileResult {
    private String template;
    /**
     * the object that the template result is holding , not required
     */
    private Object obj;
    /* rendered result */
    private String string;
    private File file;
    private Map parameters = Maps.newHashMap();

    public TemplateEngineResult() {
    }

    public static TemplateEngineResult of(String template) {
        return of(TemplateEngineResult.class, template);
    }

    public static TemplateEngineResult ofResource(Class templateClass, Class clz, String resourceName, String destDir) throws IOException {
        return of(templateClass, ReflectionUtils.loadResource(clz, resourceName)).setFile(new File(destDir, resourceName));
    }

    public static <T extends TemplateEngineResult> T of(Class<T> t, String template) {
        try {
            T re = t.newInstance();
            re.setTemplate(template);
            return re;
        } catch (Exception e) {
            throw new CodemaRuntimeException("error create template string for class : " + t.getName());
        }
    }

    public TemplateEngineResult bind(Map map) {
        if (map != null)
            parameters.putAll(map);
        return this;
    }

    public TemplateEngineResult bind(String key, Object value) {
        parameters.put(key, value);
        return this;
    }

    protected void beforeRender(Map bindingParameters) {
    }

    protected void afterRender() {
    }

    @Override
    public Object getResult() {
        return obj;
    }

    @Override
    public String getString() {
        if (string == null)
            render();
        return string;
    }

    public TemplateEngineResult render() {
        if (string != null)
            return this;
        //convert args to map binding with the template engine
        //        if (!(obj instanceof Map)) {
        //            try {
        //                map.putAll(ReflectionUtils.object2map(obj));
        //            } catch (Exception e) {
        //                e.printStackTrace();
        //                throw new CodemaRuntimeException("failed to convert arg to map: " + JSON.toJSONString(obj), e);
        //            }
        //        } else {
        //            map.putAll((Map) obj);
        //        }
        beforeRender(parameters);
        TemplateEngine templateEngine = TemplateEngineFactory.create(template);
        for (Object o : parameters.keySet()) {
            templateEngine.bind(o.toString(), parameters.get(o));
        }
        string = templateEngine.render();
        afterRender();
        return this;
    }

    @Override
    public File getFile() {
        return file;
    }

    protected void setTemplate(String template) {
        this.template = template;
    }

    public TemplateEngineResult obj(Object arg) {
        this.obj = arg;
        return this;
    }

    public Object getObj() {
        return obj;
    }

    public TemplateEngineResult setFile(File file) {
        this.file = file;
        return this;
    }

    public String getTemplate() {
        return template;
    }


    public TemplateEngineResult setParameters(Map parameters) {
        this.parameters = parameters;
        return this;
    }
}
