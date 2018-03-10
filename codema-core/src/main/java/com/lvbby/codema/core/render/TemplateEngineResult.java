package com.lvbby.codema.core.render;

import com.google.common.collect.Maps;
import com.lvbby.codema.core.error.CodemaRuntimeException;
import com.lvbby.codema.core.result.BasicResult;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Created by lipeng on 2017/1/3.
 */
public class TemplateEngineResult<T> extends BasicResult<T> {
    @Getter
    @Setter
    private String template;
    /* rendered result */
    private Map<String,Object> parameters = Maps.newHashMap();
    protected String string;

    protected TemplateEngineResult() {
    }

    public TemplateEngineResult(String template) {
        this.template = template;
    }

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
    public TemplateEngineResult bindIf(boolean expr,String key, Object value) {
        if(expr){
            bind(key,value);
        }
        return this;
    }

    protected void beforeRender(Map bindingParameters) {
    }

    protected void afterRender() {
    }

    @Override
    public void doInit() {
        beforeRender(parameters);
        doRender();
        afterRender();
    }

    private void doRender() {
        TemplateEngine templateEngine = TemplateEngineFactory.create(template);
        for (Object o : parameters.keySet()) {
            templateEngine.bind(o.toString(), parameters.get(o));
        }
        string = templateEngine.render();
    }

    public TemplateEngineResult template(String template) {
        this.template = template;
        return this;
    }

    public TemplateEngineResult setParameters(Map parameters) {
        this.parameters = parameters;
        return this;
    }

    protected void setString(String string) {
        this.string = string;
    }

    @Override
    protected String doGetString() {
        return string;
    }
}
