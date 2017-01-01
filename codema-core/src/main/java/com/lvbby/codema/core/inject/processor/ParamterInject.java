package com.lvbby.codema.core.inject.processor;

import com.lvbby.codema.core.inject.CodemaInjectContext;
import com.lvbby.codema.core.inject.CodemaInjector;
import com.lvbby.codema.core.inject.InjectParameterFactory;
import com.lvbby.codema.core.inject.Parameter;
import com.lvbby.codema.core.resource.CodemaResource;
import com.lvbby.codema.core.utils.JavaUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by lipeng on 2017/1/1.
 */
public class ParamterInject implements CodemaInjector {

    @Override
    public void process(CodemaInjectContext context) throws Exception {
        System.out.println("fuck");
        context.getArgs().stream().filter(injectEntry -> injectEntry.getParameter().isAnnotationPresent(Parameter.class)).forEach(injectEntry -> {
            Parameter annotation = injectEntry.getParameter().getAnnotation(Parameter.class);
            if (StringUtils.isBlank(annotation.value()))
                return;
            Object bean = context.getContext().getCodema().getResourceLoader().getBean(annotation.value());
            if (bean == null && annotation.createFactory() != null) {
                InjectParameterFactory instance = JavaUtils.instance(annotation.createFactory());
                CodemaResource codemaResource = instance.create(context, annotation.value());
                if (codemaResource != null) {
                    bean = codemaResource.getResource();
                    //注册到容器
                    context.getContext().getCodema().getResourceLoader().register(codemaResource);
                }
            }
            //注入参数bean
            injectEntry.setValue(bean);
        });
    }
}
