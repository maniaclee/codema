package com.lvbby.codema.java.baisc;

import com.lvbby.codema.core.VoidType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lipeng on 2016/12/22.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TemplateResource {
    Class value() default VoidType.class;

    String resource() default "";
}
