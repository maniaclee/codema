package com.lvbby.codema.java.template.annotaion;

import java.lang.annotation.*;

/**
 * Created by lipeng on 2016/12/22.
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ForeachGroup {
    /**
     * for condition
     */
    Foreach[] value();
}
