/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.lvbby.codema.app.mysql;

import com.lvbby.codema.core.AbstractCodemaMachine;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.CodemaMachine;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.render.SqlTemplateResult;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;

/**
 *
 * @author dushang.lp
 * @version $Id: MysqlSchemaCodemaConfig.java, v 0.1 2017-09-13 ÏÂÎç5:07 dushang.lp Exp $
 */
public class MysqlSchemaCodemaConfig extends CommonCodemaConfig {

    @Override
    public CodemaMachine loadCodemaMachine() {

        return new AbstractCodemaMachine() {
            @Override
            public void code(CodemaContext codemaContext,
                             CommonCodemaConfig config) throws Exception {
                Object source = codemaContext.getSource();
                if (source instanceof SqlTable) {
                    config.handle(codemaContext, new SqlTemplateResult()
                        .template(loadResourceAsString("create.sql")).bind("table", source));
                }
            }
        };
    }
}