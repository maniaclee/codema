package com.lvbby.codema.java.app.baisc;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.java.entity.JavaClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2016/12/24.
 */
public class JavaSourceParam {
    private List<JavaClass> classes = Lists.newArrayList();

    public JavaSourceParam() {
    }

    /***
     * 从javaSourceParam中加载资源
     * @param pack
     * @return
     */
    public List<JavaClass> getJavaClass(String pack) {
        if (StringUtils.isBlank(pack))
            return Lists.newArrayList(classes);
        return classes.stream().filter(javaClass -> javaClass.getPack().startsWith(pack)).collect(Collectors.toList());
    }

    /***
     * 从上下文中加载资源
     * @param codemaContext
     * @param config
     * @return
     */
    public List<JavaClass> getJavaClasses(CodemaContext codemaContext, JavaBasicCodemaConfig config) {
        return getJavaClasses(codemaContext, config.getFromPackage());
    }

    /***
     * 从上下文中加载指定package的资源
     * @param codemaContext
     * @param pack
     * @return
     */
    public List<JavaClass> getJavaClasses(CodemaContext codemaContext, String pack) {

        /** 根据config来筛选需要处理的source */
        List<JavaClass> sources = getJavaClass(pack);

        /** 从容器里找 */
        sources.addAll(codemaContext.getCodema().getCodemaBeanFactory().getBeans(codemaBean -> StringUtils.isBlank(pack) || codemaBean.getId().startsWith(pack), JavaClass.class));
        return sources;
    }

    public JavaSourceParam(List<JavaClass> classes) {
        this.classes = classes;
        if (classes == null)
            this.classes = Lists.newLinkedList();
    }

    public JavaSourceParam add(JavaClass javaClass) {
        classes.add(javaClass);
        return this;
    }

    public List<JavaClass> getClasses() {
        return classes;
    }

    public void setClasses(List<JavaClass> classes) {
        this.classes = classes;
    }
}
