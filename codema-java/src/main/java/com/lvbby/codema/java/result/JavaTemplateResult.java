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
    private TemplateContext templateContext;

    public JavaTemplateResult(JavaBasicCodemaConfig config, Class<?> javaSrcTemplate) {
        this(config, javaSrcTemplate, null);
    }

    public JavaTemplateResult(JavaBasicCodemaConfig config, Class<?> javaSrcTemplate, JavaClass javaClass) {
        this(new TemplateContext(javaSrcTemplate, config, javaClass));
    }

    public JavaTemplateResult(TemplateContext templateContext) {
        super(JavaSrcTemplateParser.instance.loadSrcTemplate(templateContext).replaceAll("\\(\\s+", "("));
        this.templateContext = templateContext;
        bind("config", templateContext.getJavaBasicCodemaConfig());
        if (templateContext.getSource() != null) {
            bind(JavaSrcTemplateParser.instance.getArgs4te(templateContext.getSource(), templateContext.getJavaBasicCodemaConfig()));
        }
    }


    @Override
    protected void afterRender() {
        registerResult();
        File file = buildJavaFile(templateContext.getJavaBasicCodemaConfig());
        if (file != null)
            setFile(file);
    }

    /**
     * register the generated result to the container , so that other module can make use of
     */
    public JavaTemplateResult registerResult() {
        if (getObj() != null)
            return this;
        JavaClass javaClass = JavaClassUtils.convert(JavaLexer.read(getString()));
        obj(javaClass);
        return this;
    }


    private File buildJavaFile(JavaBasicCodemaConfig config) {
        String destSrcRoot = config.getDestSrcRoot();
        if (StringUtils.isBlank(destSrcRoot))
            return null;
        File file = new File(destSrcRoot);
        /** file 以生成的为主 */
        JavaClass javaClass = (JavaClass) getObj();
        if (javaClass != null) {
            return _javaFile(file, javaClass.getPack(), javaClass.getName());
        }
        return null;
    }

    private File _javaFile(File file, String pack, String name) {
        file = new File(file, pack.replace('.', '/'));
        return new File(file, name + ".java");
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

}
