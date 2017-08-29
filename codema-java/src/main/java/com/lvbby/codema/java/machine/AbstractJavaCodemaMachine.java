package com.lvbby.codema.java.machine;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.AbstractCodemaMachine;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.baisc.JavaSourceParam;
import com.lvbby.codema.java.entity.JavaClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by dushang.lp on 2017/8/16.
 */
public abstract class AbstractJavaCodemaMachine<T extends JavaBasicCodemaConfig> extends
        AbstractCodemaMachine<T> {
    @Override
    public void code(CodemaContext context, T config) throws Exception {
        preCode(context, config);
        //根据包名从source里面捞取
        List<JavaClass> sources = findBeansByPackage(context, config.getFromPackage());
        for (JavaClass javaClass : sources) {
            codeEach(context, config, javaClass);
        }
    }

    private List<JavaClass> findBeansByPackage(CodemaContext codemaContext, String pack) {
        JavaSourceParam source = codemaContext.getSourceByType(JavaSourceParam.class);
        if (source == null)
            return Lists.newLinkedList();
        if (StringUtils.isBlank(pack))
            return source.getClasses();
        /** 根据config来筛选需要处理的source */
        return source.getJavaClass(pack);
    }

    protected abstract void codeEach(CodemaContext context, T config, JavaClass javaClass)
            throws Exception;

    protected void preCode(CodemaContext context, T config) throws Exception {
    }
}
