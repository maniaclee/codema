package com.lvbby.codema.java.machine;

import com.lvbby.codema.core.TemplateCapable;
import com.lvbby.codema.core.resource.ResourceLoader;
import com.lvbby.codema.core.utils.CodemaCacheHolder;
import com.lvbby.codema.java.baisc.TemplateResource;
import com.lvbby.codema.java.entity.JavaClass;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.function.Supplier;

/**
 * 入参JavaClass
 * Created by dushang.lp on 2017/8/16.
 */
public abstract class AbstractJavaInputMachine<O> extends AbstractJavaBaseMachine<JavaClass, O> implements TemplateCapable {
    private String template;

    @Override
    public String getTemplate() {
        if (StringUtils.isBlank(template)) {
            String key = String.format("cach_template_%s", getClass().getName());
            Object cache = CodemaCacheHolder.getCache().get(key);
            if (cache != null) {
                return (String) cache;
            }
            TemplateResource annotation = getClass().getAnnotation(TemplateResource.class);
            if (annotation != null) {
//                if (annotation.value() != null && !annotation.value().equals(VoidType.class)) {
//                    this.template = JavaSrcLoader.loadJavaSrcFromProjectAsString(annotation.value().getName());
//                }
                if (StringUtils.isNotBlank(annotation.resource())) {
                    try {
                        this.template = IOUtils.toString(ResourceLoader.instance.load(annotation.resource()).getInputStream());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                if (this.template != null) {
                    CodemaCacheHolder.getCache().put(key, template);
                }
            }
        }
        Validate.notBlank(template, "template can't be blank");
        return template;
    }

    @Override
    public void setTemplate(String template) {
        this.template = template;
    }

    public Supplier<String> getDestJavaClassFullNameFuture() {
        return () -> getDestJavaClassFullName();
    }

    /***
     * 获取结果java的全类名
     * @return
     */
    public String getDestJavaClassFullName() {
        if (getJavaClassNameParser() == null) {
            return null;
        }
        String className = getJavaClassNameParser().getClassName(source);
        if (!className.contains(".") && StringUtils.isNotBlank(getDestPackage())) {
            className = String.format("%s.%s", getDestPackage(), className);
        }
        return className;
    }
}
