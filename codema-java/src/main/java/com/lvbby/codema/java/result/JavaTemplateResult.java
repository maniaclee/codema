package com.lvbby.codema.java.result;

import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.core.CodemaContextHolder;
import com.lvbby.codema.core.render.TemplateEngineResult;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.resource.JavaClassResource;
import com.lvbby.codema.java.template.JavaSrcTemplateParser;
import com.lvbby.codema.java.template.TemplateContext;
import com.lvbby.codema.java.tool.AutoImport;
import com.lvbby.codema.java.tool.JavaClassUtils;
import com.lvbby.codema.java.tool.JavaLexer;
import com.lvbby.codema.java.tool.templateEngine.CodemaJavaSourcePrinter;

import java.util.Map;
import java.util.Set;

/**
 * Created by lipeng on 17/1/6.
 */
public class JavaTemplateResult extends TemplateEngineResult {
    private TemplateContext templateContext;
    private CompilationUnit compilationUnit;

    public JavaTemplateResult(JavaBasicCodemaConfig config, Class<?> javaSrcTemplate) {
        this(config, javaSrcTemplate, null);
    }

    public JavaTemplateResult(JavaBasicCodemaConfig config, Class<?> javaSrcTemplate, JavaClass javaClass) {
        this(new TemplateContext(javaSrcTemplate, config, javaClass));
    }

    public JavaTemplateResult(TemplateContext templateContext) {
        compilationUnit = JavaSrcTemplateParser.instance.loadSrcTemplateRaw(templateContext);
        this.templateContext = templateContext;
        //bind默认的参数
        bind(JavaSrcTemplateParser.instance.getArgs4te(templateContext.getSource(),
            templateContext.getJavaBasicCodemaConfig()));
        filePath(templateContext.getJavaBasicCodemaConfig().getDestSrcRoot());
    }

    @Override
    protected void beforeRender(Map bindingParameters) {
        String template = CodemaJavaSourcePrinter.toJavaSource(compilationUnit);
        template = ReflectionUtils.replace(template, "/\\*\\s*#(\\}+)\\*/\\s*([^;]+);",
                matcher -> matcher.group(2) + ";//<%" + matcher.group(1) + "%>");
        super.beforeRender(bindingParameters);
        template(JavaSrcTemplateParser.prepareTemplate(template));
    }

    @Override
    protected void afterRender() {
        CompilationUnit cu = JavaLexer.read(getString());
        //自动import
        AutoImport autoImport = new AutoImport(cu);
        //import beanFactory里的bean
        CodemaContextHolder.getCodemaContext().getCodemaBeanFactory().getBeans(JavaClass.class)
            .forEach(javaClass -> autoImport.addCandidate(javaClass));
        autoImport.parse();

        setString(cu.toString());

        JavaClass javaClass = JavaClassUtils.convert(JavaLexer.read(getString()));
        if (templateContext.getSource() != null)
            javaClass.setFrom(templateContext.getSource().getFrom());
        //注册result
        result(javaClass);
        //处理dest file
        filePath(javaClass.getPack().replace('.', '/'), javaClass.getName() + ".java");
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

    public JavaTemplateResult addImport(JavaClass importClass) {
        compilationUnit.addImport(importClass.classFullName());
        return this;
    }


}
