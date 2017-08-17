package com.lvbby.codema.java.machine;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.CodemaMachine;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaCodemaUtils;

import java.util.List;

/**
 * Created by dushang.lp on 2017/8/16.
 */
public abstract class AbstractJavaCodemaMachine<T extends JavaBasicCodemaConfig> implements CodemaMachine<T> {
    @Override public void code(CodemaContext context, T config) throws Exception {
        List<JavaClass> sources = JavaCodemaUtils.findBeansByPackage(context, config);
        for (JavaClass javaClass : sources) {
            processSingle(context, config, javaClass);
        }
    }

    protected abstract void processSingle(CodemaContext context, T config, JavaClass javaClass)
            throws Exception;
}
