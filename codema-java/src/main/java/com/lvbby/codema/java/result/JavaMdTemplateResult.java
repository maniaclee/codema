package com.lvbby.codema.java.result;

import com.lvbby.codema.core.render.TemplateEngineResult;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.template.JavaSrcTemplateParser;

/**
 *
 * @author dushang.lp
 * @version $Id: JavaMdTemplateResult.java, v 0.1 2017-09-03 下午5:26 dushang.lp Exp $
 */
public class JavaMdTemplateResult extends TemplateEngineResult {

    public static JavaMdTemplateResult ofResource(JavaBasicCodemaConfig config, String resource, JavaClass javaClass) {
        JavaMdTemplateResult re = TemplateEngineResult.of(JavaMdTemplateResult.class, resource);
        if (javaClass != null)
            re.bind(JavaSrcTemplateParser.instance.getArgs4te(javaClass, config));
        return re;
    }
}