package com.lvbby.codema.app.mysql;

import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.core.tool.mysql.entity.SqlColumn;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaClassUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.stream.Collectors;

/**
 * 产生insert语句
 * @author dushang.lp
 * @version $Id: MysqlSchemaCodemaConfig.java, v 0.1 2017-09-13  dushang.lp Exp $
 */
public class MysqlInsertMachine extends AbstractBaseMachine<Object, String> {

    /** ? */
    public static final String format_placeHolder = "format_placeHolder";
    @Getter
    @Setter
    private             String format             = format_placeHolder;

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
            String values = getValues(table, format);
            handle(BasicResult.instance(String.format("insert into %s(\n%s\n) \nvalues(\n%s\n);",
                    table.getNameInDb(),
                    columns,
                    values)));
        }

    }

    private String getValues(SqlTable table, String format) {
        switch (format) {
            case format_placeHolder:
                return table.getFields().stream().map(sqlColumn -> "?")
                        .collect(Collectors.joining(",\n"));
        }
        throw new IllegalArgumentException(String.format("unknown format: %s", format));
    }

}