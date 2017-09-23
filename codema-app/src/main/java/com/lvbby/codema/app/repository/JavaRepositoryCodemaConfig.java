package com.lvbby.codema.app.repository;

import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.baisc.JavaClassNameParser;
import com.lvbby.codema.java.baisc.JavaClassNameParserFactory;

/**
 * Created by lipeng on 2017/1/7.
 */
@ConfigBind(JavaRepositoryCodemaMachine.class)
public class JavaRepositoryCodemaConfig extends JavaBasicCodemaConfig {
    private JavaClassNameParser convertUtilsClass = JavaClassNameParserFactory.className("BuildUtils");

    public JavaClassNameParser getConvertUtilsClass() {
        return convertUtilsClass;
    }

    public void setConvertUtilsClass(JavaClassNameParser convertUtilsClass) {
        this.convertUtilsClass = convertUtilsClass;
    }
}
