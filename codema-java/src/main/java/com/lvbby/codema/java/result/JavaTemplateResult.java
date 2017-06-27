package com.lvbby.codema.java.result;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.render.TemplateEngineResult;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.entity.JavaAnnotation;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaType;
import com.lvbby.codema.java.template.ForeachSub;
import com.lvbby.codema.java.template.JavaSrcTemplateParser;
import com.lvbby.codema.java.template.TemplateContext;
import com.lvbby.codema.java.tool.ImportUtils;
import com.lvbby.codema.java.tool.JavaClassUtils;
import com.lvbby.codema.java.tool.JavaCodemaUtils;
import com.lvbby.codema.java.tool.JavaLexer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    /***
     * 对模板进行处理
     * @param compilationUnit
     */
    private void processForeach(CompilationUnit compilationUnit) {
        JavaLexer.getClass(compilationUnit).ifPresent(clz -> {
            clz.getFields().forEach(fieldDeclaration -> {
                NodeList<AnnotationExpr> annotationByClass = fieldDeclaration.getAnnotations();
                List<JavaAnnotation> annotations = Lists.newArrayList();
                for (int i = annotationByClass.size() - 1; i >= 0; i--) {
                    if (ForeachSub.class.getSimpleName().equals(annotationByClass.get(i).getNameAsString())) {
                        annotations.add(JavaLexer.parseAnnotation(annotationByClass.get(i)));
                        annotationByClass.remove(i);
                    }
                }
                if (!annotations.isEmpty())
                    JavaLexer.appendFieldCommentAdEnd(fieldDeclaration, "#");
                for (JavaAnnotation javaAnnotation : annotations) {
                    JavaLexer.addComment(fieldDeclaration, false,
                            String.format("for(%s){", javaAnnotation.get(JavaAnnotation.defaultPropertyName).toString()));
                    JavaLexer.addComment(fieldDeclaration, false,
                            javaAnnotation.getList("body").stream().map(Object::toString).collect(Collectors.joining(";")));
                    JavaLexer.appendFieldCommentAdEnd(fieldDeclaration, "}");
                }

            });
        });
        System.out.println(compilationUnit);
    }

    @Override
    protected void beforeRender(Map bindingParameters) {
        processForeach(compilationUnit);
        super.beforeRender(bindingParameters);
        String template = ReflectionUtils.replace(compilationUnit.toString(), "/\\*\\s*#(\\}+)\\*/\\s*([^;]+);",
                matcher -> matcher.group(2) + ";<%" + matcher.group(1) + "%>");
        System.err.println(template);
        setTemplate(JavaSrcTemplateParser.prepareTemplate(template));
    }

    @Override
    protected void afterRender() {
        //imports
        CompilationUnit cu = JavaLexer.read(getString());
        new ImportUtils().tryAddImports(cu);
        setString(cu.toString());

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
        if (templateContext.getSource() != null)
            javaClass.setFrom(templateContext.getSource().getFrom());
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
