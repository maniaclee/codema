package com.lvbby.codema.core.config;

import java.lang.annotation.*;

/**
 * Created by lipeng on 2016/12/22.
 * 一个CodemaMachine需绑定一个Config
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfigBind {
    Class<?> value();
}
