package com.lvbby.codema.java.result;

import com.lvbby.codema.core.render.TemplateEngineResult;
import com.lvbby.codema.core.render.XmlTemplateResult;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.template.JavaSrcTemplateParser;

import java.io.File;

/**
 * Created by lipeng on 2017/1/14.
 */
public class JavaXmlTemplateResult extends XmlTemplateResult {

    public JavaXmlTemplateResult(JavaBasicCodemaConfig config, String resource, JavaClass javaClass) {
        super(resource);
        if (javaClass != null){
            bind(JavaSrcTemplateParser.instance.getArgs4te(javaClass, config));
        }
    }
}
