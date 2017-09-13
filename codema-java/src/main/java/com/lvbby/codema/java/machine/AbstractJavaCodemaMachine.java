package com.lvbby.codema.java.machine;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.AbstractCodemaMachine;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.entity.JavaClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by dushang.lp on 2017/8/16.
 */
public abstract class AbstractJavaCodemaMachine<T extends JavaBasicCodemaConfig>
                                               extends AbstractCodemaMachine<T> {
    @Override
    public void code(CodemaContext context, T config) throws Exception {
        preCode(context, config);
        //根据from捞取要处理的bean作为入口对象
        for (JavaClass javaClass : findTargetJavaClasses(context, config)) {
            codeEach(context, config, javaClass);
        }
    }

    protected abstract void codeEach(CodemaContext context, T config,
                                     JavaClass javaClass) throws Exception;

    protected void preCode(CodemaContext context, T config) throws Exception {
    }

    /***
     * 如果from为空，取source，否则从容器里取
     */
    private List<JavaClass> findTargetJavaClasses(CodemaContext context, T config) {
        String fromPackage = config.getFromPackage();
        if (StringUtils.isBlank(fromPackage))
            return Lists.newArrayList((JavaClass) context.getSource());
        return context.getCodemaBeanFactory().getBeans(JavaClass.class,
            codemaBean -> codemaBean.getId().startsWith(fromPackage));
    }
}
