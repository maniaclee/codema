package com.lvbby.codema.core;

import com.lvbby.codema.core.render.TemplateEngineResult;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 基本可用的模板machine
 * Created by lipeng on 2018/3/22.
 */
public class BasicTemplateMachine<S, O> extends AbstractTemplateMachine<S, O> {
    @Getter
    @Setter
    private Map<String, Object> args;

    @Override
    protected void doCode() throws Exception {
        TemplateEngineResult result = templateEngineResult(getTemplate());
        result.bind("source", source)
                .bind(args);
        handle(result);
    }

    /**
     * 可被子类覆写
     *
     * @param template
     * @return
     */
    public TemplateEngineResult<O> templateEngineResult(String template) {
        return new TemplateEngineResult(template);
    }

}
