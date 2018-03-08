package com.lvbby.codema.java.result;

import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.render.TemplateEngineResult;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaBaseMachine;
import com.lvbby.codema.java.template.JavaSrcTemplateParser;

/**
 *
 * @author dushang.lp
 * @version $Id: JavaMdTemplateResult.java, v 0.1 2017-09-03 下午5:26 dushang.lp Exp $
 */
public class JavaBasicTemplateResult<O> extends TemplateEngineResult<O> {

    public JavaBasicTemplateResult(AbstractBaseMachine config, String template, JavaClass javaClass) {
        super(template);
        if (javaClass != null){
            bind(JavaSrcTemplateParser.instance.getArgs4te(javaClass, config));
        }
    }
}