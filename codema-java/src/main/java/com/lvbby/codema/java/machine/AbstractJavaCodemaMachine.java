package com.lvbby.codema.java.machine;

import com.lvbby.codema.core.AbstractBaseCodemaMachine;
import com.lvbby.codema.core.CodemaBeanFactorytHolder;
import com.lvbby.codema.core.CodemaMachine;
import com.lvbby.codema.core.bean.CodemaBean;
import com.lvbby.codema.core.config.ConfigProperty;
import com.lvbby.codema.core.result.Result;
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
public abstract class AbstractJavaCodemaMachine<S,O>
        extends AbstractBaseCodemaMachine<S, O> {

    /**
     * 目标package
     */
    @ConfigProperty
    private String destPackage;

    /***
     * 获取目标bean的名称
     */
    @ConfigProperty
    private JavaClassNameParser javaClassNameParser = JavaClassNameParserFactory.defaultInstance();

    @Override protected void handle(Result result) throws Exception {
        super.handle(result);
        //注册java class 到容器
        if(result!=null && result.getResult()!=null && result.getResult()instanceof JavaClass){
            CodemaBeanFactorytHolder.get().getCodemaBeanFactory().register(new CodemaBean((JavaClass)result.getResult(),o -> o.classFullName()));
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
        CodemaMachine<String, JavaClass> sourceMachine = JavaClassMachineFactory.fromClassFullName()
                .source(classFullName);
        sourceMachine.code();
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
    public AbstractJavaCodemaMachine<S, O> destPackage(String destPackage) {
        this.destPackage = destPackage;
        return this;
    }

    public JavaClassNameParser getJavaClassNameParser() {
        return javaClassNameParser;
    }

    public void setJavaClassNameParser(JavaClassNameParser javaClassNameParser) {
        this.javaClassNameParser = javaClassNameParser;
    }
    public AbstractJavaCodemaMachine<S, O>javaClassNameParser(JavaClassNameParser javaClassNameParser) {
        this.javaClassNameParser = javaClassNameParser;
        return this;
    }

}
