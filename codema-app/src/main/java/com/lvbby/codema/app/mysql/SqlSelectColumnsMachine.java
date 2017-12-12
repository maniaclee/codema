package com.lvbby.codema.app.mysql;

import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.core.tool.mysql.entity.SqlColumn;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaClassUtils;

import java.util.stream.Collectors;

/**
 * 产生select %s from %s 语句
 * @author dushang.lp
 * @version $Id: MysqlSchemaCodemaConfig.java, v 0.1 2017-09-13  dushang.lp Exp $
 */
public class SqlSelectColumnsMachine extends AbstractBaseMachine<Object, String> {

    @Override public void doCode() throws Exception {
        SqlTable table = null;
        if (source instanceof SqlTable) {
            table = (SqlTable) source;
        }
        if (source instanceof JavaClass) {
            table = JavaClassUtils.convertToSqlTable((JavaClass) source);
        }
        if (table != null) {
            String columns = table.getFields().stream().map(SqlColumn::getNameInDb)
                    .collect(Collectors.joining(",\n"));
            handle(BasicResult.instance(String.format("select\n %s \nfrom %s", columns, table.getNameInDb())));
        }

    }

}