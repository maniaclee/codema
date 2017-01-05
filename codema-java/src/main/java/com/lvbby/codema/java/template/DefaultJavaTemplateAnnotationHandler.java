package com.lvbby.codema.java.template;

import com.lvbby.codema.core.utils.TypeCapable;

import java.lang.annotation.Annotation;

/**
 * Created by lipeng on 17/1/5.
 */
public abstract class DefaultJavaTemplateAnnotationHandler<T extends Annotation> extends TypeCapable<T> implements JavaTemplateAnnotationHandler {

    @Override
    public boolean annotation(Annotation annotation) {
        return isType(annotation);
    }

    @Override
    public String getString(Annotation annotation) {
        return evalString((T) annotation);
    }

    public abstract String evalString(T t);
}
