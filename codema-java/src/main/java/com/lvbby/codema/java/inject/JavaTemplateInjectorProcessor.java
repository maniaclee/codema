package com.lvbby.codema.java.inject;

import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.core.inject.CodemaInjectContext;
import com.lvbby.codema.core.inject.CodemaInjectorProcessor;
import com.lvbby.codema.core.inject.InjectEntry;
import com.lvbby.codema.core.inject.InjectInterruptException;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.baisc.JavaSourceParam;
import com.lvbby.codema.java.tool.JavaClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2016/12/27.
 */
public class JavaTemplateInjectorProcessor implements CodemaInjectorProcessor {
    public static final String java_source = "java_source";
    public static final String java_dest = "java_dest";

    @Override
    public void process(CodemaInjectContext context) throws Exception {
        if (!context.getCodeRunnerMethod().isAnnotationPresent(JavaTemplate.class))
            return;
        Object source = context.getContext().getSource();
        if (source == null || !(source instanceof JavaSourceParam))
            return;
        /** 从参数中找到制定的Config */
        JavaBasicCodemaConfig config = findConfig(context);
        if (config == null)
            return;
        /** 根据config来筛选需要处理的source */
        List<CompilationUnit> sources = ((JavaSourceParam) source).getCompilationUnits().stream().filter(u -> filterPackage(u, config)).collect(Collectors.toList());
        for (CompilationUnit compilationUnit : sources) {
            /** 对每个source，分别调用改方法，自动把foreach干掉 */
            List<InjectEntry> cloneEntries = context.cloneEntries();
            cloneEntries.stream()
                    .forEach(injectEntry -> injectCompilationUnit(context, config, compilationUnit, injectEntry));
            context.invoke(cloneEntries);//出错直接抛出去
        }
        throw new InjectInterruptException("interrupted by " + getClass().getName());
    }

    private JavaBasicCodemaConfig findConfig(CodemaInjectContext context) {
        return context.getArgs().stream().filter(injectEntry -> JavaBasicCodemaConfig.class.isAssignableFrom(injectEntry.getParameter().getType())).findFirst().map(injectEntry -> (JavaBasicCodemaConfig) injectEntry.getValue()).orElse(null);
    }

    private static boolean filterPackage(CompilationUnit compilationUnit, JavaBasicCodemaConfig config) {
        if (StringUtils.isBlank(config.getFromPackage()))
            return true;
        return compilationUnit.getPackage().map(p -> p.getPackageName()).map(p -> p.startsWith(config.getFromPackage())).orElse(false);
    }


    private static void injectCompilationUnit(CodemaInjectContext context, JavaBasicCodemaConfig config, CompilationUnit compilationUnitSource, InjectEntry injectEntry) {
        Parameter annotation = injectEntry.getParameter().getAnnotation(Parameter.class);
        if (annotation != null && StringUtils.isNotBlank(annotation.identifier())) {
            switch (annotation.identifier()) {
                case java_source:
                    injectEntry.setValue(compilationUnitSource);
                    break;
                case java_dest:
                    injectEntry.setValue(JavaClassUtils.createJavaClasss(context.getContext(), config, compilationUnitSource));
                    break;
            }
        }
    }


}
