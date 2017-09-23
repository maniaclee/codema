package com.lvbby.codema.java.machine;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.AbstractCodemaMachine;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.entity.JavaClass;
import org.apache.commons.collections.CollectionUtils;
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
        //捞取
        if (StringUtils.isNotBlank(config.getFromPackage())) {
            List<JavaClass> beans = context.getCodemaBeanFactory().getBeans(JavaClass.class,
                codemaBean -> codemaBean.getId().startsWith(config.getFromPackage()));
            for (JavaClass javaClass : beans) {
                codeEach(context, config, javaClass);
            }
            return;
        }
        List froms = super.loadFrom(context, config);
        if(CollectionUtils.isNotEmpty(froms)){
            for (Object from : froms) {
                if(from instanceof JavaClass){
                    codeEach(context, config, (JavaClass) from);
                }
            }
            return;
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
            return Lists.newArrayList();
        return context.getCodemaBeanFactory().getBeans(JavaClass.class,
            codemaBean -> codemaBean.getId().startsWith(fromPackage));
    }
}
