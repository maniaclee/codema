package com.lvbby.codema.core.inject;

import java.lang.annotation.*;

/**
 * Created by lipeng on 2016/12/22.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Parameter {
    String value();

    Class<? extends InjectParameterFactory> createFactory() default EmptyInjectParameterFactory.class;
}
