package com.lvbby.codema.core.render;

import java.util.Map;

/**
 *
 * @author dushang.lp
 * @version $Id: SqlTemplateResult.java, v 0.1 2017-09-13 下午5:21 dushang.lp Exp $
 */
public class SqlTemplateResult extends TemplateEngineResult {
    @Override
    protected void beforeRender(Map bindingParameters) {
        //取消注释
        template(getTemplate().replaceAll("#", ""));
    }
}