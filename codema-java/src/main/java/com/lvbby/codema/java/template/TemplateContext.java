package com.lvbby.codema.java.template;

import com.lvbby.codema.java.app.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.entity.JavaClass;

/**
 * Created by lipeng on 2017/1/14.
 */
public class TemplateContext {
    private JavaClass source;
    private Class templateClass;
    private JavaBasicCodemaConfig javaBasicCodemaConfig;
    private String pack;

    public TemplateContext(Class templateClass, JavaBasicCodemaConfig javaBasicCodemaConfig) {
        this(templateClass, javaBasicCodemaConfig, null);
    }

    public TemplateContext(Class templateClass, JavaBasicCodemaConfig javaBasicCodemaConfig, JavaClass source) {
        this.source = source;
        this.templateClass = templateClass;
        this.javaBasicCodemaConfig = javaBasicCodemaConfig;
    }

    public TemplateContext pack(String pack) {
        setPack(pack);
        return this;
    }

    public Class getTemplateClass() {
        return templateClass;
    }

    public void setTemplateClass(Class templateClass) {
        this.templateClass = templateClass;
    }

    public JavaBasicCodemaConfig getJavaBasicCodemaConfig() {
        return javaBasicCodemaConfig;
    }

    public void setJavaBasicCodemaConfig(JavaBasicCodemaConfig javaBasicCodemaConfig) {
        this.javaBasicCodemaConfig = javaBasicCodemaConfig;
    }

    public JavaClass getSource() {
        return source;
    }

    public void setSource(JavaClass source) {
        this.source = source;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }
}
