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

    public static JavaTemplateResult ofJavaClass(JavaBasicCodemaConfig config, Class<?> javaSrcTemplate, JavaClass javaClass) {
        String template = JavaSrcTemplateParser.instance.loadSrcTemplate(new TemplateContext(javaSrcTemplate, config, javaClass));
        template = template.replaceAll("\\(\\s+", "("); //format (
        return ofTemplate(config, template, javaClass);
    }


    public static JavaTemplateResult ofTemplate(JavaBasicCodemaConfig config, String template, JavaClass javaClass) {
        JavaTemplateResult re = TemplateEngineResult.of(JavaTemplateResult.class, template);
        if (javaClass != null)
            re.bind(JavaSrcTemplateParser.instance.getArgs4te(javaClass, config));
        re.setFile(buildFile(config, javaClass));
        return re;
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
        obj(JavaClassUtils.convert(JavaLexer.read(getString())));
        return this;
    }


    public static File buildFile(JavaBasicCodemaConfig config, JavaClass javaClass) {
        String destSrcRoot = config.getDestSrcRoot();
        if (StringUtils.isBlank(destSrcRoot))
            return null;
        File file = new File(destSrcRoot);
        if (!file.isDirectory() || !file.exists())
            return null;
        if (StringUtils.isNotBlank(javaClass.getPack())) {
            file = new File(file, javaClass.getPack().replace('.', '/'));
        }
        return new File(file, javaClass.getName() + ".java");
    }

}
