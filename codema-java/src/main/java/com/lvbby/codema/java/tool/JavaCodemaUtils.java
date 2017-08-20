package com.lvbby.codema.java.tool;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.baisc.JavaSourceParam;
import com.lvbby.codema.java.entity.JavaClass;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by lipeng on 2017/1/18.
 */
public class JavaCodemaUtils {

    public static List<JavaClass> findBeansByPackage(CodemaContext codemaContext, JavaBasicCodemaConfig config) {
        return findBeansByPackage(codemaContext, config.getFromPackage());
    }

    public static List<JavaClass> findBeansByPackage(CodemaContext codemaContext, String pack) {
        JavaSourceParam source = codemaContext.getSourceByType(JavaSourceParam.class);
        if (source == null)
            return Lists.newLinkedList();
        if (StringUtils.isBlank(pack))
            return source.getClasses();
        /** 根据config来筛选需要处理的source */
        List<JavaClass> sources = source.getJavaClass(pack);
        /** 从容器里找 */
        sources.addAll(codemaContext.getCodema().getCodemaBeanFactory().getBeans(codemaBean -> StringUtils.isBlank(pack) || codemaBean.getId().startsWith(pack), JavaClass.class));
        return sources;
    }

    public static JavaClass findBeanByJavaClassName(CodemaContext context, String className) {
        if (StringUtils.isBlank(className))
            return null;
        List<JavaClass> beans = context.getCodema().getCodemaBeanFactory().getBeans(codemaBean -> codemaBean.getId().endsWith(className), JavaClass.class);
        return CollectionUtils.isNotEmpty(beans) ? beans.get(0) : null;
    }

}
