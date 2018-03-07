package com.lvbby.codema.app;

import com.lvbby.codema.java.machine.impl.JavaTemplateMachine;
import com.lvbby.codema.java.tool.JavaSrcLoader;

/**
 *
 * @author dushang.lp
 * @version $Id: AppMachine.java, v 0.1 2018年03月07日 下午4:36 dushang.lp Exp $
 */
public class AppMachine extends JavaTemplateMachine {
    {
        AppTemplateResource annotation = getClass().getAnnotation(AppTemplateResource.class);
        if(annotation!=null){
            setTemplate(JavaSrcLoader.loadJavaSrcFromProjectAsString(annotation.value().getName()));
        }
    }
}