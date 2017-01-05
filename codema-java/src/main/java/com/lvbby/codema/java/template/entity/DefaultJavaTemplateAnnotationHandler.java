package com.lvbby.codema.java.template.entity;

import java.lang.annotation.Annotation;

/**
 * Created by lipeng on 17/1/5.
 */
public class DefaultJavaTemplateAnnotationHandler implements JavaTemplateAnnotationHandler {
    Class<? extends Annotation> annotationClass;
    @Override
    public boolean annotation(Annotation annotation) {
        return false;
    }

    @Override
    public String getString() {
        return null;
    }
}
