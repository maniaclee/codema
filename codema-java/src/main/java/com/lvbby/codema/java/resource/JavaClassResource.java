package com.lvbby.codema.java.resource;

import com.lvbby.codema.core.bean.CodemaBean;
import com.lvbby.codema.java.entity.JavaClass;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by lipeng on 2016/12/31.
 */
public class JavaClassResource extends CodemaBean {
    public JavaClassResource(JavaClass javaClass) {
        super(javaClass);
        String className = javaClass.getName();
        String id = StringUtils.isBlank(javaClass.getPack()) ? className : (javaClass.getPack() + "." + className);
        setId(id);
        initType(false, false);
    }
}
