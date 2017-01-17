package com.lvbby.codema.java.result;

import com.lvbby.codema.core.render.TemplateEngineResult;
import com.lvbby.codema.java.app.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.template.JavaSrcTemplateParser;
import com.lvbby.codema.java.template.TemplateContext;
import com.lvbby.codema.java.tool.JavaClassUtils;
import com.lvbby.codema.java.tool.JavaLexer;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Map;

/**
 * Created by lipeng on 17/1/6.
 */
public class JavaTemplateResult extends TemplateEngineResult {
    private JavaBasicCodemaConfig config;

    public static JavaTemplateResult ofJavaClass(JavaBasicCodemaConfig config, Class<?> javaSrcTemplate) {
        return ofJavaClass(config, javaSrcTemplate, null);
    }

    public static JavaTemplateResult ofJavaClass(JavaBasicCodemaConfig config, Class<?> javaSrcTemplate, JavaClass javaClass) {
        String template = JavaSrcTemplateParser.instance.loadSrcTemplate(new TemplateContext(javaSrcTemplate, config, javaClass));
        template = template.replaceAll("\\(\\s+", "("); //format (
        return ofTemplate(config, template, javaClass);
    }

    public static JavaTemplateResult ofTemplateContext(TemplateContext templateContext) {
        String template = JavaSrcTemplateParser.instance.loadSrcTemplate(templateContext);
        template = template.replaceAll("\\(\\s+", "("); //format (
        return ofTemplate(templateContext.getJavaBasicCodemaConfig(), template, templateContext.getSource());
    }


    public static JavaTemplateResult ofTemplate(JavaBasicCodemaConfig config, String template, JavaClass javaClass) {
        JavaTemplateResult re = TemplateEngineResult.of(JavaTemplateResult.class, template);
        re.setConfig(config);
        re.bind(JavaSrcTemplateParser.instance.getArgs4te(javaClass, config));
        return re;
    }

    @Override
    protected void afterRender() {
        setFile(buildFile(config, (JavaClass) getObj()));
    }

    /***
     * to achieve stream api
     * */
    @Override
    public JavaTemplateResult bind(Map map) {
        return (JavaTemplateResult) super.bind(map);
    }

    @Override
    public JavaTemplateResult bind(String key, Object value) {
        return (JavaTemplateResult) super.bind(key, value);
    }

    /**
     * register the generated result to the container , so that other module can make use of
     */
    public JavaTemplateResult registerResult() {
        JavaClass javaClass = JavaClassUtils.convert(JavaLexer.read(getString()));
        obj(javaClass);
        return this;
    }


    public static File buildFile(JavaBasicCodemaConfig config, JavaClass javaClass) {
        String destSrcRoot = config.getDestSrcRoot();
        if (StringUtils.isBlank(destSrcRoot))
            return null;
        File file = new File(destSrcRoot);
        if (!file.isDirectory() || !file.exists())
            return null;
        if (javaClass == null) {
            System.out.println("----------------------- error TODO ------------------");
            return null;//TODO
        }
        if (StringUtils.isNotBlank(javaClass.getPack())) {
            file = new File(file, javaClass.getPack().replace('.', '/'));
        }
        return new File(file, javaClass.getName() + ".java");
    }

    public JavaBasicCodemaConfig getConfig() {
        return config;
    }

    public void setConfig(JavaBasicCodemaConfig config) {
        this.config = config;
    }
}
