package com.lvbby.codema.java.baisc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author dushang.lp
 * @version $Id: JavaTemplateResource.java, v 0.1 2017-09-01 обнГ3:54 dushang.lp Exp $$
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JavaTemplateResource {
    Class  value();
}