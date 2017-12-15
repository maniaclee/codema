package com.lvbby.codema.core.tool.mysql;

import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.Machine;
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

    public static Machine<String, SqlTable> fromSqlCreate() {
        return new AbstractBaseMachine<String, SqlTable>() {
            @Override protected void doCode() throws Exception {
                for (SqlTable table : SqlParser.fromSql(source)) {
                    //调用处理器，但是不设置result
                    handleSimple(BasicResult.instance(table));
                    //出发后续流程
                    invokeNext(table);
                }
            }
        };
    }

    public static Machine<SqlTable, SqlTable> fromTable(SqlTable sqlTable) {
        return new AbstractBaseMachine<SqlTable, SqlTable>() {
            @Override protected void doCode() throws Exception {
                setResult(BasicResult.instance(source));
            }
        }.source(sqlTable);
    }

    public static List<Machine<SqlTable, SqlTable>> fromJdbcUrl(String url, String userName,
                                                                String password,
                                                                String filter)
            throws Exception {
        List<SqlTable> tables = UrlJdbcTableFactory.of(url, userName, password)
                .tableRegularFilter(filter).getTables();
        return tables.stream().map(table -> fromTable(table)).collect(Collectors.toList());

    }
}