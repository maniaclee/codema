package com.lvbby.codema.core.tool.mysql;

import com.lvbby.codema.core.AbstractBaseCodemaMachine;
import com.lvbby.codema.core.CodemaMachine;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author dushang.lp
 * @version $Id: SqlMachineFactory.java, v 0.1 2017-12-03 下午2:43 dushang.lp Exp $
 */
public class SqlMachineFactory {

    public static List<CodemaMachine<SqlTable, SqlTable>> fromSqlCreate(String sqlCreate) {
        return SqlParser.fromSql(sqlCreate).stream().map(table -> fromTable(table))
                .collect(Collectors.toList());
    }

    public static CodemaMachine<SqlTable, SqlTable> fromTable(SqlTable sqlTable) {
        return new AbstractBaseCodemaMachine<SqlTable, SqlTable>() {
            @Override protected void doCode() throws Exception {
                setResult(BasicResult.instance(source));
            }
        }.source(sqlTable);
    }

    public static List<CodemaMachine<SqlTable, SqlTable>> fromJdbcUrl(String url, String userName,
                                                                      String password,
                                                                      String filter)
            throws Exception {
        List<SqlTable> tables = UrlJdbcTableFactory.of(url, userName, password)
                .tableRegularFilter(filter).getTables();
        return tables.stream().map(table -> fromTable(table)).collect(Collectors.toList());

    }
}