package com.lvbby.codema.java.result;

import com.google.common.collect.Maps;
import com.lvbby.codema.core.error.CodemaRuntimeException;
import com.lvbby.codema.core.render.TemplateEngine;
import com.lvbby.codema.core.render.TemplateEngineFactory;
import com.lvbby.codema.core.result.FileResult;
import com.lvbby.codema.core.result.PrintableResult;
import com.lvbby.codema.java.app.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.template.JavaSrcTemplateParser;
import com.lvbby.codema.java.tool.JavaClassUtils;
import com.lvbby.codema.java.tool.JavaLexer;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Map;

/**
 * Created by lipeng on 17/1/6.
 */
public class JavaTemplateResult implements PrintableResult, FileResult {

    private String stringContent;
    private JavaBasicCodemaConfig config;
    private JavaClass javaClass;
    /**
     * 返回的结果，可以向容器里注册以供其他模块使用
     */
    private Object result;


    public JavaTemplateResult(JavaBasicCodemaConfig config, Class<?> javaSrcTemplate, JavaClass javaClass) {
        this(config, javaSrcTemplate, javaClass, null);
    }

    public JavaTemplateResult(JavaBasicCodemaConfig config, Class<?> javaSrcTemplate, JavaClass javaClass, Map map) {
        this.config = config;
        this.javaClass = javaClass;

        String template = JavaSrcTemplateParser.instance.loadSrcTemplate(javaSrcTemplate, config);
        if (map == null)
            map = Maps.newHashMap();
        if (javaClass != null)
            map.putAll(JavaSrcTemplateParser.instance.getArgs4te(javaClass, config));

        TemplateEngine te = TemplateEngineFactory.create(template);
        Map finalMap = map;
        map.keySet().forEach(o -> te.bind(o.toString(), finalMap.get(o)));
        stringContent = te.render();
        stringContent = stringContent.replaceAll("\\(\\s+", "("); //format (
    }

    /**
     * register the generated result to the container , so that other module can make use of
     */
    public JavaTemplateResult registerResult() {
        if (result == null)
            result = JavaClassUtils.convert(JavaLexer.read(stringContent));
        return this;
    }

    @Override
    public File getFile() {
        String destSrcRoot = config.getDestSrcRoot();
        if (StringUtils.isBlank(destSrcRoot))
            throw new CodemaRuntimeException("file dir is empty");
        File file = new File(destSrcRoot);
        if (!file.isDirectory() || !file.exists())
            throw new CodemaRuntimeException("file dir not existed");
        if (StringUtils.isNotBlank(javaClass.getPack())) {
            file = new File(file, javaClass.getPack().replace('.', '/'));
        }
        return new File(file, javaClass.getName() + ".java");
    }

    public JavaTemplateResult result(Object result) {
        this.result = result;
        return this;
    }

    @Override
    public String getString() {
        return stringContent;
    }

    @Override
    public Object getResult() {
        return result;
    }
}
