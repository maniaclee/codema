package com.lvbby.codema.core.render;

import com.google.common.collect.Maps;
import com.lvbby.codema.core.error.CodemaRuntimeException;
import com.lvbby.codema.core.result.BasicResult;

import java.util.Map;

/**
 * Created by lipeng on 2017/1/3.
 */
public class TemplateEngineResult<T> extends BasicResult<T> {
    private String template;
    /* rendered result */
    private String string;
    private Map parameters = Maps.newHashMap();
    private transient boolean rendered = false;

    public static <T extends TemplateEngineResult> T of(Class<T> t, String template) {
        try {
            T re = t.newInstance();
            re.template(template);
            return re;
        } catch (Exception e) {
            throw new CodemaRuntimeException(
                    "error create template string for class : " + t.getName());
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
    public String getString() {
        if (!rendered) {
            beforeRender(parameters);
            render();
            rendered = true;
            afterRender();
        }
        return string;
    }

    public TemplateEngineResult render() {
        TemplateEngine templateEngine = TemplateEngineFactory.create(template);
        for (Object o : parameters.keySet()) {
            templateEngine.bind(o.toString(), parameters.get(o));
        }
        string = templateEngine.render();
        return this;
    }

    public TemplateEngineResult template(String template) {
        this.template = template;
        return this;
    }

    public String getTemplate() {
        return template;
    }

    public TemplateEngineResult setParameters(Map parameters) {
        this.parameters = parameters;
        return this;
    }

    public Map getParameters() {
        return parameters;
    }

    protected void setString(String string) {
        this.string = string;
    }
}
