package com.lvbby.codema.java.machine;

import com.lvbby.codema.core.AbstractCodemaMachine;
import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.entity.JavaClass;

/**
 * 入参和出参都是JavaClass
 * Created by dushang.lp on 2017/8/16.
 */
public abstract class AbstractJavaCodemaMachine<T extends JavaBasicCodemaConfig>
        extends AbstractCodemaMachine<T, JavaClass, JavaClass> {

    @Override public void code(T config, JavaClass javaClass) throws Exception {
        //result
        Result<JavaClass> re = codeEach(config,javaClass);
        //设置result
        setResult(re);
        //调用handler处理
        config.handle(re);
    }

    protected abstract Result<JavaClass> codeEach(T config, JavaClass javaClass) throws Exception;

}
