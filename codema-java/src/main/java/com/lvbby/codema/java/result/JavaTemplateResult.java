package com.lvbby.codema.java.result;

import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.core.CodemaContextHolder;
import com.lvbby.codema.core.render.TemplateEngineResult;
import com.lvbby.codema.core.result.MergeCapableFileResult;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaCodemaMachine;
import com.lvbby.codema.java.template.JavaSrcTemplateParser;
import com.lvbby.codema.java.tool.AutoImport;
import com.lvbby.codema.java.tool.JavaClassUtils;
import com.lvbby.codema.java.tool.JavaLexer;
import com.lvbby.codema.java.tool.JavaMerger;
import com.lvbby.codema.java.tool.templateEngine.CodemaJavaSourcePrinter;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by lipeng on 17/1/6.
 */
public class JavaTemplateResult extends TemplateEngineResult implements MergeCapableFileResult{
    private CompilationUnit compilationUnit;

    public JavaTemplateResult(AbstractJavaCodemaMachine config, Class<?> javaSrcTemplate, JavaClass javaClass) {
        compilationUnit = JavaSrcTemplateParser.instance.loadSrcTemplateRaw(config,javaSrcTemplate);
        //bind默认的参数
        bind(JavaSrcTemplateParser.instance.getArgs4te(javaClass,config));
        filePath(config.getDestRootDir());
    }

    @Override
    protected void beforeRender(Map bindingParameters) {
        String template = CodemaJavaSourcePrinter.toJavaSource(compilationUnit);
        template = ReflectionUtils.replace(template, "/\\*\\s*#(\\}+)\\*/\\s*([^;]+);",
                matcher -> matcher.group(2) + ";//<%" + matcher.group(1) + "%>");
        super.beforeRender(bindingParameters);
        template(JavaSrcTemplateParser.prepareTemplate(template));
        System.err.println(template);
    }

    @Override
    protected void afterRender() {
        CompilationUnit cu = null;
        try {
            cu = JavaLexer.read(getString());
        } catch (Exception e) {
            System.out.println("==============");
            System.out.println(getString());
            throw e;
        }
        //自动import
        AutoImport autoImport = new AutoImport(cu);
        //import beanFactory里的bean
        CodemaContextHolder.getCodemaContext().getCodemaBeanFactory().getBeans(JavaClass.class)
            .forEach(javaClass -> autoImport.addCandidate(javaClass));
        autoImport.parse();

        setString(cu.toString());

        JavaClass javaClass = JavaClassUtils.convert(JavaLexer.read(getString()));
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

    @Override
    public String parseMergeResult(InputStream dest) throws Exception {
        return new JavaMerger(IOUtils.toString(dest), JavaLexer.read(getString())).merge().toString();
    }
}
