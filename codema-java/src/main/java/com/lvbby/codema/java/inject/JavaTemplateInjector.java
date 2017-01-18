package com.lvbby.codema.java.inject;

import com.lvbby.codema.core.inject.CodemaInjectContext;
import com.lvbby.codema.core.inject.CodemaInjector;
import com.lvbby.codema.core.inject.InjectEntry;
import com.lvbby.codema.core.inject.InjectInterruptException;
import com.lvbby.codema.core.utils.OrderValue;
import com.lvbby.codema.java.app.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.app.baisc.JavaSourceParam;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaCodemaUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by lipeng on 2016/12/27.
 */
@OrderValue(4000)
public class JavaTemplateInjector implements CodemaInjector {
    public static final String java_source = "java_source";

    @Override
    public void process(CodemaInjectContext context) throws Exception {
        if (!context.getCodeRunnerMethod().isAnnotationPresent(JavaTemplate.class))
            return;
        Object source = context.getContext().getSource();
        if (source == null || !(source instanceof JavaSourceParam))
            return;
        /** 从参数中找到制定的Config */
        JavaBasicCodemaConfig config = context.findConfig(JavaBasicCodemaConfig.class);
        if (config == null)
            return;

        List<JavaClass> sources = JavaCodemaUtils.findBeansByPackage(context.getContext(), config);

        for (JavaClass javaClass : sources) {
            /** 对每个source，分别调用改方法，自动把foreach干掉 */
            List<InjectEntry> cloneEntries = context.cloneEntries();
            cloneEntries.stream().forEach(injectEntry -> injectCompilationUnit(context, config, javaClass, injectEntry));
            context.invoke(cloneEntries);//出错直接抛出去
        }
        throw new InjectInterruptException("interrupted by " + getClass().getName());
    }


    private static void injectCompilationUnit(CodemaInjectContext context, JavaBasicCodemaConfig config, JavaClass javaClass, InjectEntry injectEntry) {
        JavaTemplateParameter annotation = injectEntry.getParameter().getAnnotation(JavaTemplateParameter.class);
        if (annotation != null && StringUtils.isNotBlank(annotation.identifier())) {
            switch (annotation.identifier()) {
                case java_source:
                    injectEntry.setValue(javaClass);
                    break;
            }
        }
    }


}
