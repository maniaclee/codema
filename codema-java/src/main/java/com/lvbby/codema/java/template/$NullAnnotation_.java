package com.lvbby.codema.java.template;

import java.lang.annotation.*;

/**
 * Created by lipeng on 2016/12/22.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable($NullAnnotationContainer_.class)
public @interface $NullAnnotation_ {

    String EndIf = "<%}%>";

    String value() default EndIf;
}
