package com.lvbby.codema.java.machine;

import com.lvbby.codema.core.AbstractTemplateMachine;
import com.lvbby.codema.core.CodemaContextHolder;
import com.lvbby.codema.core.Machine;
import com.lvbby.codema.core.TemplateCapable;
import com.lvbby.codema.core.bean.CodemaBean;
import com.lvbby.codema.core.config.ConfigProperty;
import com.lvbby.codema.core.resource.ResourceLoader;
import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.java.api.JavaSourceMachineFactory;
import com.lvbby.codema.java.baisc.JavaClassNameParser;
import com.lvbby.codema.java.baisc.TemplateResource;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.result.JavaTemplateResult;
import com.lvbby.codema.java.template.JavaSrcTemplateParser;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.List;

/**
 * 具有java的配置信息
 * Created by dushang.lp on 2017/8/16.
 */
public abstract class AbstractJavaBaseMachine
        extends AbstractTemplateMachine<JavaClass, JavaClass> implements TemplateCapable {
    /**
     * 目标package
     */
    @ConfigProperty
    @Getter
    @Setter
    private String destPackage;

    /***
     * 获取目标bean的名称
     */
    @ConfigProperty
    @Getter
    @Setter
    private JavaClassNameParser<JavaClass> destClassName = source1 -> source1.getName();

    @Override
    protected void handle(Result result) throws Exception {
        super.handle(result);
        //注册java class 到容器
        if (result != null && result.getResult() != null && result.getResult() instanceof JavaClass) {
            CodemaContextHolder.get().getCodemaBeanFactory().register(new CodemaBean(result.getResult(), o -> ((JavaClass)o).classFullName()));
        }
    }

    @Override
    protected void doCode() throws Exception {
        JavaTemplateResult result = codeEach(source);
        Validate.notBlank(result.getPack(),"target pack can't be empty: %s",getClass().getName());
        Validate.notBlank(result.getDestClassName(),"target class name can't be empty",getClass().getName());
        handle(result);
    }

    public abstract JavaTemplateResult codeEach(JavaClass cu) throws Exception;

    protected JavaTemplateResult buildJavaTemplateResult() throws Exception {
        return JavaTemplateResult.fromMachine(this)
                .bind(JavaSrcTemplateParser.instance.getArgs4te(source, this));
    }

    public String parseDestClassName(JavaClass javaClass) {
        return destClassName.getClassName(javaClass);
    }

    public String parseDestClassFullName(JavaClass javaClass) {
        return String.format("%s.%s", getDestPackage(), parseDestClassName(javaClass));
    }

    public AbstractJavaBaseMachine destPackage(String destPackage) {
        this.destPackage = destPackage;
        return this;
    }

    public AbstractJavaBaseMachine javaClassNameParser(JavaClassNameParser<JavaClass> javaClassNameParser) {
        this.destClassName = javaClassNameParser;
        return this;
    }

    @Override
    public String getTemplate() {
        if (StringUtils.isBlank(super.getTemplate()) && getClass().isAnnotationPresent(TemplateResource.class)) {
            TemplateResource annotation = getClass().getAnnotation(TemplateResource.class);
            try {
                setTemplate(IOUtils.toString(ResourceLoader.instance.load(annotation.resource()).getInputStream()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return super.getTemplate();
    }

    /***
     * 查找java 对象，从容器里或本地
     * @param classFullName
     * @return
     * @throws Exception
     */
    @Deprecated
    protected JavaClass findJavaBean(String classFullName) throws Exception {
        if (StringUtils.isBlank(classFullName))
            return null;
        boolean isFullName = classFullName.contains(".");
        //1. 从容器里找
        if (!isFullName) {
            List<JavaClass> beans = findBean(JavaClass.class,
                    javaClass -> StringUtils.equals(classFullName, javaClass.getName()));
            if (CollectionUtils.isEmpty(beans)) {
                return null;
            }
            if (beans.size() == 1) {
                return beans.get(0);
            }
            throw new RuntimeException(String.format("multi beans found for %s", classFullName));
        }
        JavaClass bean = findBeanAny(JavaClass.class,
                javaClass -> StringUtils.equals(classFullName, javaClass.classFullName()));
        if (bean != null) {
            return bean;
        }
        //2. 从本地找
        Machine<String, JavaClass> sourceMachine = JavaSourceMachineFactory.fromClassFullName()
                .source(classFullName);
        sourceMachine.run();
        Result<JavaClass> result = sourceMachine.getResult();
        if (result != null && result.getResult() != null) {
            return result.getResult();
        }
        return bean;
    }
}
