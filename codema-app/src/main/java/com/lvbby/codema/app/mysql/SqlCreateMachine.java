package com.lvbby.codema.app.mysql;

import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.render.SqlTemplateResult;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaClassUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * 生成sql create
 * @author dushang.lp
 * @version $Id: MysqlSchemaCodemaConfig.java, v 0.1 2017-09-13  dushang.lp Exp $
 */
public class SqlCreateMachine extends AbstractBaseMachine<Object, String> {
    @Getter
    @Setter
    private String primaryKey = "id";

    @Override
    public void doCode() throws Exception {
        SqlTable table = null;
        if (source instanceof SqlTable)
            table = (SqlTable) source;

        if (source instanceof JavaClass)
            table = JavaClassUtils.convertToSqlTable((JavaClass) source);

        if (table != null) {
            if (StringUtils.isNotBlank(primaryKey))
                table.buildPrimaryKeyField(primaryKey);
            Validate.notNull(table.getPrimaryKey(), "primary id can't be null");
            handle(new SqlTemplateResult().template(loadResourceAsString("create.sql")).bind("table", table));
        }

    }

}