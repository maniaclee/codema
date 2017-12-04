package com.lvbby.codema.app.mysql;

import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.render.SqlTemplateResult;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 *
 * @author dushang.lp
 * @version $Id: MysqlSchemaCodemaConfig.java, v 0.1 2017-09-13  dushang.lp Exp $
 */
public class MysqlSchemaMachine extends AbstractBaseMachine<Object, String> {
    private String primaryKey;

    @Override public void doCode() throws Exception {
        SqlTable table = null;
        if (source instanceof SqlTable) {
            table = (SqlTable) source;
        }
        if (source instanceof JavaClass) {
            table = JavaClassUtils.convertToSqlTable((JavaClass) source);
        }
        if (table != null) {
            if (StringUtils.isNotBlank(primaryKey)) {
                table.buildPrimaryKeyField(primaryKey);
            }
            Validate.notNull(table.getPrimaryKeyField(), "primary id can't be null");
            //TODO TemplateResult result 模型有问题，太复杂
            handle(new SqlTemplateResult().template(loadResourceAsString("create.sql"))
                    .bind("table", table));
        }

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