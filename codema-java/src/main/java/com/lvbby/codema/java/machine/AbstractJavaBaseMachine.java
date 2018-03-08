package com.lvbby.codema.java.machine;

import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.CodemaContextHolder;
import com.lvbby.codema.core.Machine;
import com.lvbby.codema.core.TemplateCapable;
import com.lvbby.codema.core.bean.CodemaBean;
import com.lvbby.codema.core.config.ConfigProperty;
import com.lvbby.codema.core.resource.ResourceLoader;
import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.core.utils.CodemaCacheHolder;
import com.lvbby.codema.java.api.JavaSourceMachineFactory;
import com.lvbby.codema.java.baisc.JavaClassNameParser;
import com.lvbby.codema.java.baisc.JavaClassNameParserFactory;
import com.lvbby.codema.java.baisc.TemplateResource;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.result.JavaTemplateResult;
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
        extends AbstractBaseMachine<JavaClass, JavaClass> implements TemplateCapable {

    @Setter
    private String template;
    /**
     * 目标package
     */
    @ConfigProperty
    @Getter
    @Setter
    private String destPackage;
    /**
     * 目标package
     */
    @ConfigProperty
    @Getter
    @Setter
    private String author = System.getProperty("user.name");

    /***
     * 获取目标bean的名称
     */
    @ConfigProperty
    @Getter
    @Setter
    private JavaClassNameParser javaClassNameParser = JavaClassNameParserFactory.defaultInstance();

    @Override
    protected void handle(Result<JavaClass> result) throws Exception {
        super.handle(result);
        //注册java class 到容器
        if (result != null && result.getResult() != null && result.getResult() instanceof JavaClass) {
            CodemaContextHolder.get().getCodemaBeanFactory().register(new CodemaBean(result.getResult(), o -> o.classFullName()));
        }
    }

    @Override
    protected void doCode() throws Exception {
        handle(codeEach(source));
    }

    public abstract JavaTemplateResult codeEach(JavaClass cu) throws Exception;

    protected JavaTemplateResult buildJavaTemplateResult() throws Exception {
        return new JavaTemplateResult(this, getTemplate(), getSource());
    }


    public String parseDestClassName(JavaClass javaClass) {
        return javaClassNameParser.getClassName(javaClass);
    }

    public String parseDestClassFullName(JavaClass javaClass) {
        return String.format("%s.%s", getDestPackage(), parseDestClassName(javaClass));
    }

    public AbstractJavaBaseMachine destPackage(String destPackage) {
        this.destPackage = destPackage;
        return this;
    }

    public AbstractJavaBaseMachine javaClassNameParser(JavaClassNameParser javaClassNameParser) {
        this.javaClassNameParser = javaClassNameParser;
        return this;
    }

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
