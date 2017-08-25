package com.lvbby.codema.core.render;

import java.util.Map;

/**
 * Created by lipeng on 2017/1/14.
 */
public class XmlTemplateResult extends TemplateEngineResult {

    @Override
    protected void beforeRender(Map bindingParameters) {
        template(getTemplate().replaceAll("<!--\\s*", "").replaceAll("\\s*-->", ""));
    }

}
