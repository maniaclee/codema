package com.lvbby.codema.java.result;

import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.render.TemplateEngineResult;
import com.lvbby.codema.core.utils.FileUtils;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaType;
import com.lvbby.codema.java.template.JavaSrcTemplateParser;
import com.lvbby.codema.java.template.TemplateContext;
import com.lvbby.codema.java.tool.JavaClassUtils;
import com.lvbby.codema.java.tool.JavaCodemaUtils;
import com.lvbby.codema.java.tool.JavaLexer;
import com.lvbby.codema.java.tool.templateEngine.CodemaJavaSourcePrinter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Collection;
import java.util.Map;

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
        bind("config", templateContext.getJavaBasicCodemaConfig());
        if (templateContext.getSource() != null) {
            bind(JavaSrcTemplateParser.instance.getArgs4te(templateContext.getSource(), templateContext.getJavaBasicCodemaConfig()));
        }
        filePath(templateContext.getJavaBasicCodemaConfig().getDestSrcRoot());
    }


    public JavaTemplateResult addImportClassFullName(Collection<String> importClass) {
        if (CollectionUtils.isNotEmpty(importClass))
            importClass.forEach(i -> addImport(i));
        return this;
    }

    public JavaTemplateResult addImport(String importClass) {
        if (ReflectionUtils.isFullClassName(importClass))
            compilationUnit.addImport(importClass);
        return this;
    }

    public JavaTemplateResult addImport(JavaClass importClass) {
        compilationUnit.addImport(importClass.classFullName());
        return this;
    }

    public JavaTemplateResult addImportJavaClasses(Collection<JavaClass> importClass) {
        if (CollectionUtils.isNotEmpty(importClass))
            importClass.forEach(i -> addImport(i));
        return this;
    }

    public JavaTemplateResult addImport(Class importClass) {
        compilationUnit.addImport(importClass);
        return this;
    }

    public JavaTemplateResult addImport(String className, CodemaContext codemaContext) {
        /** 如果是全类名 */
        if (ReflectionUtils.isFullClassName(className))
            return addImport(className);
        JavaClass clz = JavaCodemaUtils.findBeanByJavaClassName(codemaContext, className);
        if (clz != null)
            addImport(clz);
        return this;
    }

    public JavaTemplateResult addImport(JavaType type, CodemaContext codemaContext) {
        if (type.getJavaType() != null)
            return addImport(type.getJavaType());
        JavaClass clz = JavaCodemaUtils.findBeanByJavaClassName(codemaContext, type.getSpecificType());
        if (clz != null)
            addImport(clz);
        return this;
    }


    @Override
    protected void beforeRender(Map bindingParameters) {
        String template = CodemaJavaSourcePrinter.toJavaSource(compilationUnit);
        template = ReflectionUtils.replace(template, "/\\*\\s*#(\\}+)\\*/\\s*([^;]+);",
                matcher -> matcher.group(2) + ";//<%" + matcher.group(1) + "%>");
        super.beforeRender(bindingParameters);
        System.err.println(template);
        setTemplate(JavaSrcTemplateParser.prepareTemplate(template));
    }

    @Override
    protected void afterRender() {
        //imports
        System.out.println("afterRender");
        System.out.println(getString());
        CompilationUnit cu = JavaLexer.read(getString());
//        new ImportUtils().tryAddImports(cu);
        setString(cu.toString());

        registerResult();
    }

    /**
     * register the generated result to the container , so that other module can make use of
     */
    public JavaTemplateResult registerResult() {
        if (getResult() != null)
            return this;
        JavaClass javaClass = JavaClassUtils.convert(JavaLexer.read(getString()));
        if (templateContext.getSource() != null)
            javaClass.setFrom(templateContext.getSource().getFrom());
        result(javaClass);
        //处理dest file
        filePath(javaClass.getPack().replace('.', '/'),javaClass.getName()+".java");
        return this;
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
