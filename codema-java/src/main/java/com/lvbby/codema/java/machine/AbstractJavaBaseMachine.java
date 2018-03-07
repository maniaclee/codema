package com.lvbby.codema.java.machine;

import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.CodemaContextHolder;
import com.lvbby.codema.core.Machine;
import com.lvbby.codema.core.bean.CodemaBean;
import com.lvbby.codema.core.config.ConfigProperty;
import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.java.api.JavaSourceMachineFactory;
import com.lvbby.codema.java.baisc.JavaClassNameParser;
import com.lvbby.codema.java.baisc.JavaClassNameParserFactory;
import com.lvbby.codema.java.entity.JavaClass;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 具有java的配置信息
 * Created by dushang.lp on 2017/8/16.
 */
public abstract class AbstractJavaBaseMachine<S,O>
        extends AbstractBaseMachine<S, O> {

    /**
     * 目标package
     */
    @ConfigProperty
    private String destPackage;
    /**
     * 目标package
     */
    @ConfigProperty
    private String author = System.getProperty("user.name");

    /***
     * 获取目标bean的名称
     */
    @ConfigProperty
    private JavaClassNameParser javaClassNameParser = JavaClassNameParserFactory.defaultInstance();

    @Override
    protected void handle(Result result) throws Exception {
        super.handle(result);
        //注册java class 到容器
        if(result!=null && result.getResult()!=null && result.getResult()instanceof JavaClass){
            CodemaContextHolder.get().getCodemaBeanFactory().register(new CodemaBean((JavaClass)result.getResult(), o -> o.classFullName()));
        }
    }

    /***
     * 查找java 对象，从容器里或本地
     * @param classFullName
     * @return
     * @throws Exception
     */
    protected JavaClass  findJavaBean(String classFullName) throws Exception {
        if(StringUtils.isBlank(classFullName))
            return null;
        boolean isFullName = classFullName.contains(".");
        //1. 从容器里找
        if(!isFullName){
            List<JavaClass> beans = findBean(JavaClass.class,
                    javaClass -> StringUtils.equals(classFullName, javaClass.getName()));
            if(CollectionUtils.isEmpty(beans)){
                return null;
            }
            if(beans.size()==1){
                return beans.get(0);
            }
            throw new RuntimeException(String.format("multi beans found for %s", classFullName));
        }
        JavaClass bean = findBeanAny(JavaClass.class,
                javaClass -> StringUtils.equals(classFullName, javaClass.classFullName()));
        if(bean!=null){
            return bean;
        }
        //2. 从本地找
        Machine<String, JavaClass> sourceMachine = JavaSourceMachineFactory.fromClassFullName()
                .source(classFullName);
        sourceMachine.run();
        Result<JavaClass> result = sourceMachine.getResult();
        if(result!=null && result.getResult()!=null){
            return result.getResult();
        }
        return bean;
    }

    public String parseDestClassName(JavaClass javaClass){
        return javaClassNameParser.getClassName(javaClass);
    }
    protected String parseDestClassFullName(JavaClass javaClass){
        return String.format("%s.%s", getDestPackage(),parseDestClassName(javaClass));
    }
    public String getDestPackage() {
        return destPackage;
    }

    public void setDestPackage(String destPackage) {
        this.destPackage = destPackage;
    }
    public AbstractJavaBaseMachine<S, O> destPackage(String destPackage) {
        this.destPackage = destPackage;
        return this;
    }

    public JavaClassNameParser getJavaClassNameParser() {
        return javaClassNameParser;
    }

    public void setJavaClassNameParser(JavaClassNameParser javaClassNameParser) {
        this.javaClassNameParser = javaClassNameParser;
    }
    public AbstractJavaBaseMachine<S, O> javaClassNameParser(JavaClassNameParser javaClassNameParser) {
        this.javaClassNameParser = javaClassNameParser;
        return this;
    }

    /**
     * Getter method for property   author.
     *
     * @return property value of author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Setter method for property   author .
     *
     * @param author  value to be assigned to property author
     */
    public void setAuthor(String author) {
        this.author = author;
    }
}
