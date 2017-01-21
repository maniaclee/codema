package com.lvbby.codema.java.template;

import com.lvbby.codema.core.error.CodemaRuntimeException;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.entity.JavaClass;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

/**
 * Created by lipeng on 2017/1/14.
 */
public class TemplateContext {
    private JavaClass source;
    @Deprecated
    private Class templateClass;
    private JavaBasicCodemaConfig javaBasicCodemaConfig;
    private String pack;
    private String template;

    public TemplateContext(Class templateClass, JavaBasicCodemaConfig javaBasicCodemaConfig) {
        this(templateClass, javaBasicCodemaConfig, null);
    }

    public TemplateContext(Class templateClass, JavaBasicCodemaConfig javaBasicCodemaConfig, JavaClass source) {
        this.source = source;
        this.templateClass = templateClass;
        this.javaBasicCodemaConfig = javaBasicCodemaConfig;
    }

    public TemplateContext(JavaBasicCodemaConfig javaBasicCodemaConfig, String templateClassResource) {
        this(javaBasicCodemaConfig,templateClassResource,null);
    }
    public TemplateContext(JavaBasicCodemaConfig javaBasicCodemaConfig, String templateClassResource, JavaClass source) {
        this.source = source;
        this.javaBasicCodemaConfig = javaBasicCodemaConfig;
        try {
            this.template = IOUtils.toString(javaBasicCodemaConfig.getClass().getClassLoader().getResourceAsStream(templateClassResource));
        } catch (IOException e) {
            throw new CodemaRuntimeException("failed to read template", e);
        }
    }

    public TemplateContext pack(String pack) {
        setPack(pack);
        return this;
    }

    public String findTemplate() {
        return template;
    }


    public Class getTemplateClass() {
        return templateClass;
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

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }
}
