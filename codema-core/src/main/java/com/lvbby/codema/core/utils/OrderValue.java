package com.lvbby.codema.core.utils;

import java.lang.annotation.*;

/**
 * Created by lipeng on 2016/12/22.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OrderValue {
    int value() default Integer.MAX_VALUE;
}
