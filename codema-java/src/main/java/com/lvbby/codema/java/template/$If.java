package com.lvbby.codema.java.template;

import java.lang.annotation.*;

/**
 * Created by lipeng on 2016/12/22.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface $If {
    String value();
}
