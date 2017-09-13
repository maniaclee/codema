/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.lvbby.codema.core.render;

import java.util.Map;

/**
 *
 * @author dushang.lp
 * @version $Id: SqlTemplateResult.java, v 0.1 2017-09-13 ÏÂÎç5:21 dushang.lp Exp $
 */
public class SqlTemplateResult extends TemplateEngineResult {
    @Override
    protected void beforeRender(Map bindingParameters) {
        //È¡Ïû×¢ÊÍ
        template(getTemplate().replaceAll("#", ""));
    }
}