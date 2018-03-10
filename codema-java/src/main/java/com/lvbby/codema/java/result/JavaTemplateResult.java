package com.lvbby.codema.java.result;

import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.core.CodemaContextHolder;
import com.lvbby.codema.core.render.TemplateEngineResult;
import com.lvbby.codema.core.result.MergeCapableFileResult;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaBaseMachine;
import com.lvbby.codema.java.template.JavaSrcTemplateParser;
import com.lvbby.codema.java.tool.AutoImport;
import com.lvbby.codema.java.tool.JavaClassUtils;
import com.lvbby.codema.java.tool.JavaLexer;
import com.lvbby.codema.java.tool.JavaMerger;
import com.lvbby.codema.java.tool.templateEngine.CodemaJavaSourcePrinter;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 结果为JavaClass
 * Created by lipeng on 17/1/6.
 */
public class JavaTemplateResult extends TemplateEngineResult<JavaClass> implements MergeCapableFileResult<JavaClass>{
    @Getter
    @Setter
    private String pack;
    @Getter
    @Setter
    private String destClassName;
    @Getter
    @Setter
    private String author;

    public static JavaTemplateResult fromMachine(AbstractJavaBaseMachine machine){
        return (JavaTemplateResult) new JavaTemplateResult(machine.getTemplate())
                .bindSource(machine.getSource())
                .pack(machine.getDestPackage())
                .destClassName(machine.getJavaClassNameParser().getClassName(machine.getSource()))
                .author(machine.getAuthor())
                .filePath(machine.getDestRootDir());
    }


    public JavaTemplateResult(String template) {
        super(template);
    }

    public JavaTemplateResult bindSource(Object source){
        bind("source",source);
        return this;
    }
    public JavaTemplateResult destClassName(String  destClassName){
        setDestClassName(destClassName);
        return this;
    }
    public JavaTemplateResult author(String  author){
        setAuthor(author);
        return this;
    }

    public JavaTemplateResult pack(String pack){
        setPack(pack);
        return this;
    }

    @Override
    protected void beforeRender(Map bindingParameters) {
        bind("pack",pack);
        bind("destClassName",destClassName);
        bind("author",author);
        //加载java template
        CompilationUnit compilationUnit = JavaSrcTemplateParser.instance.loadSrcTemplateRaw(JavaLexer.read(getTemplate()));
        //JavaDoc comment
        JavaLexer.getClass(compilationUnit).ifPresent(classOrInterfaceDeclaration -> {
            classOrInterfaceDeclaration.setJavadocComment(
                    String.format("\n * Created by %s on %s.\n ",getAuthor(),
                            new SimpleDateFormat("yyyy/MM/dd").format(new Date())));
        });
        //package
        if(StringUtils.isNotBlank(pack)){
            compilationUnit.setPackageDeclaration(pack);
        }

        //处理各种注解标签，输出template
        String template = CodemaJavaSourcePrinter.toJavaSource(compilationUnit);
        super.beforeRender(bindingParameters);
        template(JavaSrcTemplateParser.prepareTemplate(template));
        //处理dest file
        filePath(getPack().replace('.', '/'));
        filePath(String.format("%s.java", destClassName));

    }

    @Override
    protected void afterRender() {
        CompilationUnit cu = null;
        String resultString = string;
        try {
            cu = JavaLexer.read(resultString);
        } catch (Exception e) {
            System.out.println("==== render error =====");
            System.out.println(resultString);
            throw e;
        }
        //自动import
        AutoImport autoImport = new AutoImport(cu);
        //import beanFactory里的bean
        CodemaContextHolder.get().getCodemaBeanFactory().getBeans(JavaClass.class)
            .forEach(javaClass -> autoImport.addCandidate(javaClass));
        autoImport.parse();

        setString(cu.toString());

        //注册result
        result(JavaClassUtils.convert(cu));
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
