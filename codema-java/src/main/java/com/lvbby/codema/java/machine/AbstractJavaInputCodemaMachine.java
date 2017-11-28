package com.lvbby.codema.java.machine;

import com.lvbby.codema.core.AbstractCodemaMachine;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.entity.JavaClass;

/**
 * 入参和出参都是JavaClass
 * Created by dushang.lp on 2017/8/16.
 */
public abstract class AbstractJavaInputCodemaMachine<T extends JavaBasicCodemaConfig, O>
        extends AbstractCodemaMachine<T, JavaClass, O> {
}
