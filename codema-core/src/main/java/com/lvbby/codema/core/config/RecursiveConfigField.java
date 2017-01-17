package com.lvbby.codema.core.config;

import java.lang.annotation.*;

/**
 * Created by lipeng on 2016/12/22.
 * 可递归的config
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RecursiveConfigField {
}
