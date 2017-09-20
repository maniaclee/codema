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
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 *
 * @author dushang.lp
 * @version $Id: MysqlSchemaCodemaConfig.java, v 0.1 2017-09-13 ÏÂÎç5:07 dushang.lp Exp $
 */
public class MysqlSchemaCodemaConfig extends CommonCodemaConfig {
    private String primaryKey;

    @Override
    public CodemaMachine loadCodemaMachine() {

        return new AbstractCodemaMachine() {
            @Override
            public void code(CodemaContext codemaContext,
                             CommonCodemaConfig config) throws Exception {
                Object source = codemaContext.getSource();
                SqlTable table=null;
                if (source instanceof SqlTable) {
                    table= (SqlTable) source;
                }
                if (source instanceof JavaClass) {
                    table = JavaClassUtils.convertToSqlTable((JavaClass) source);
                }
                if (table != null) {
                    if (StringUtils.isNotBlank(primaryKey)) {
                        table.buildPrimaryKeyField(primaryKey);
                    }
                    Validate.notNull(table.getPrimaryKeyField(), "primary id can't be null");
                    config.handle(codemaContext, new SqlTemplateResult()
                        .template(loadResourceAsString("create.sql")).bind("table", table));
                }
            }
        };
    }

    /**
     * Getter method for property   primaryKey.
     *
     * @return property value of primaryKey
     */
    public String getPrimaryKey() {
        return primaryKey;
    }

    /**
     * Setter method for property   primaryKey .
     *
     * @param primaryKey  value to be assigned to property primaryKey
     */
    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
}