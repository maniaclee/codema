package com.lvbby.codema.java.template.annotaion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lipeng on 2016/12/22.
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE_USE, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(value = ForeachGroup.class)
public @interface Foreach {

    /**
     * for condition
     */
    String value();

    /**
     * body
     */
    String[] body() default "";
}