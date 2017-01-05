package com.lvbby.codema.java.template.entity;

import java.lang.annotation.Annotation;

/**
 * Created by lipeng on 17/1/5.
 */
public interface JavaTemplateAnnotationHandler {

    boolean annotation(Annotation annotation);

    String getString();

}
