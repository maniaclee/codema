package com.lvbby.codema.java.result;

import com.github.javaparser.ast.CompilationUnit;
import com.google.common.collect.Maps;
import com.lvbby.codema.core.render.TemplateEngine;
import com.lvbby.codema.core.render.TemplateEngineFactory;
import com.lvbby.codema.java.app.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.template.JavaSrcTemplateParser;

import java.util.Map;

/**
 * Created by lipeng on 17/1/6.
 */
public class JavaTemplateResult extends JavaResult {

    private final String stringContent;

    public JavaTemplateResult(JavaBasicCodemaConfig config, Class<?> javaSrcTemplate, CompilationUnit cu) {
        this(config, javaSrcTemplate, cu, null);
    }

    public JavaTemplateResult(JavaBasicCodemaConfig config, Class<?> javaSrcTemplate, CompilationUnit cu, Map map) {
        super(cu, config);
        String template = JavaSrcTemplateParser.instance.parse(javaSrcTemplate);
        if (map == null)
            map = Maps.newHashMap();
        map.putAll(JavaSrcTemplateParser.instance.getArgs4te(cu));

        TemplateEngine te = TemplateEngineFactory.create(template);
        Map finalMap = map;
        map.keySet().forEach(o -> te.bind(o.toString(), finalMap.get(o)));
        stringContent = te.render();
    }

    @Override
    public String getString() {
        return stringContent;
    }
}
