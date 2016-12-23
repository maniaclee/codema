package com.lvbby.codema.render.codebot;

import java.util.List;
import java.util.Map;

/**
 * Created by lipeng on 16/10/16.
 */
public class TemplateContext {

    private Map<String, Object> args;
    private String template;
    private List<String> handlerClasses;
    private String templateEngineClass = null;

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public List<String> getHandlerClasses() {
        return handlerClasses;
    }

    public void setHandlerClasses(List<String> handlerClasses) {
        this.handlerClasses = handlerClasses;
    }

    public String getTemplateEngineClass() {
        return templateEngineClass;
    }

    public void setTemplateEngineClass(String templateEngineClass) {
        this.templateEngineClass = templateEngineClass;
    }
}
