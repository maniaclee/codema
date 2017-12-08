package com.lvbby.codema.java.machine;

import com.lvbby.codema.core.TemplateCapable;
import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.java.baisc.TemplateResource;
import com.lvbby.codema.java.entity.JavaClass;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

/**
 * 入参JavaClass
 * Created by dushang.lp on 2017/8/16.
 */
public abstract class AbstractJavaInputMachine
        extends AbstractJavaMachine<JavaClass, JavaClass> implements TemplateCapable{
    private String template;
    @Override protected void doCode() throws Exception {
        handle(codeEach(source));
    }

    public abstract Result<JavaClass> codeEach(JavaClass cu) throws Exception;

    @Override
    public String getTemplate() {
        if(StringUtils.isBlank(template)){
            TemplateResource annotation = getClass().getAnnotation(TemplateResource.class);
            if(annotation!=null){
                return null;
            }
        }
        return template;
    }

    @Override
    public void setTemplate() {

    }

    public Supplier<String> getDestJavaClassFullNameFuture(){
        return () -> getDestJavaClassFullName();
    }
    /***
     * 获取结果java的全类名
     * @return
     */
    public String getDestJavaClassFullName(){
        if(getJavaClassNameParser()==null){
            return null;
        }
        String className = getJavaClassNameParser().getClassName(source);
        if(!className.contains(".") && StringUtils.isNotBlank(getDestPackage())){
            className= String.format("%s.%s", getDestPackage(),className);
        }
        return className;
    }
}
