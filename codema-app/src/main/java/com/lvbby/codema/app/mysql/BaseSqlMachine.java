package com.lvbby.codema.app.mysql;

import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.TemplateCapable;
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
public abstract class BaseSqlMachine extends AbstractBaseMachine<Object, String>
        implements TemplateCapable {
    private String template;

    @Override public void doCode() throws Exception {
        SqlTable table = null;
        if (source instanceof SqlTable) {
            table = (SqlTable) source;
        }
        if (source instanceof JavaClass) {
            table = JavaClassUtils.convertToSqlTable((JavaClass) source);
        }
        if (table != null) {
            handle(new SqlTemplateResult().template(getTemplate()).bind("table", table));
        }

    }

    @Override public String getTemplate() {
        return template;
    }

    @Override public void setTemplate(String template) {
        this.template = template;
    }
}