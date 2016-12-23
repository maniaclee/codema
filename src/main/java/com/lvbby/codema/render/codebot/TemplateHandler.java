package com.lvbby.codema.render.codebot;

import com.lvbby.codebot.chain.ContextHandler;
import com.lvbby.codema.render.TemplateEngine;
import com.lvbby.codema.render.TemplateEngineFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by lipeng on 16/10/16.
 */
public class TemplateHandler implements ContextHandler<TemplateContext> {
    @Override
    public void handle(TemplateContext context) {
        TemplateEngine templateEngine;
        if (StringUtils.isBlank(context.getTemplateEngineClass())) {
            templateEngine = TemplateEngineFactory.create(context.getTemplate());
        } else {
            try {
                Class aClass = Class.forName(context.getTemplateEngineClass());
                templateEngine = TemplateEngineFactory.create(aClass, context.getTemplate());
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
        if (context.getArgs() != null)
            for (Map.Entry<String, Object> entry : context.getArgs().entrySet())
                templateEngine.bind(entry.getKey(), entry.getValue());
        String render = templateEngine.render();
    }
}
