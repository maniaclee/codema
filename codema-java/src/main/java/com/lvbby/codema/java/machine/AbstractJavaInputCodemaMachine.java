package com.lvbby.codema.java.machine;

import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.core.utils.FunctionAdaptor;
import com.lvbby.codema.java.entity.JavaClass;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

/**
 * 入参JavaClass
 * Created by dushang.lp on 2017/8/16.
 */
public abstract class AbstractJavaInputCodemaMachine
        extends AbstractJavaCodemaMachine<JavaClass, JavaClass> {

    @Override protected void doCode() throws Exception {
        handle(codeEach(source));
    }

    public abstract Result<JavaClass> codeEach(JavaClass cu) throws Exception;

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
