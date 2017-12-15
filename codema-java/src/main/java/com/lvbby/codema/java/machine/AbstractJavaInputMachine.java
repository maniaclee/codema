package com.lvbby.codema.java.machine;

import com.lvbby.codema.core.TemplateCapable;
import com.lvbby.codema.core.VoidType;
import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.java.baisc.TemplateResource;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.result.JavaTemplateResult;
import com.lvbby.codema.java.tool.JavaSrcLoader;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

/**
 * 入参JavaClass
 * Created by dushang.lp on 2017/8/16.
 */
public abstract class AbstractJavaInputMachine<O>
        extends AbstractJavaMachine<JavaClass, O> implements TemplateCapable{
    private String template;


    @Override
    public String getTemplate() {
        if (StringUtils.isBlank(template)) {
            TemplateResource annotation = getClass().getAnnotation(TemplateResource.class);
            if (annotation != null) {
                if(annotation.value()!=null && !annotation.value().equals(VoidType.class)) {
                    this.template = JavaSrcLoader
                            .loadJavaSrcFromProjectAsString(annotation.value().getName());
                }
                if(StringUtils.isNotBlank(annotation.resource())){
                    this.template=loadResourceAsString(annotation.resource());
                }
            }
        }
        return template;
    }

    @Override
    public void setTemplate(String template) {
        this.template=template;
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
