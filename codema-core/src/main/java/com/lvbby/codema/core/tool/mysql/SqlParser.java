package com.lvbby.codema.core.tool.mysql;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.NotNullConstraint;
import com.alibaba.druid.sql.ast.statement.SQLColumnPrimaryKey;
import com.alibaba.druid.sql.ast.statement.SQLColumnUniqueKey;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSQLColumnDefinition;
import com.alibaba.druid.util.JdbcConstants;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.lvbby.codema.core.tool.mysql.entity.SqlColumn;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2017/1/6.
 */
public class SqlParser {

    public static List<SqlTable> fromSql(String sql) {
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        if (stmtList == null)
            return Lists.newArrayList();
        return stmtList.stream().filter(s -> s instanceof MySqlCreateTableStatement).map(sqlStatement -> {
            MySqlCreateTableStatement create = (MySqlCreateTableStatement) sqlStatement;
            SqlTable t = SqlTable.instance(create.getTableSource().toString());
            t.setFields(create.getTableElementList().stream().filter(s -> s instanceof MySqlSQLColumnDefinition).map(sqlTableElement -> buildColumn((MySqlSQLColumnDefinition) sqlTableElement)).collect(Collectors.toList()));
            t.setPrimaryKeyField(t.getFields().stream().filter(sqlColumn -> sqlColumn.isPrimaryKey()).findAny().orElse(null));
            return t;
        }).collect(Collectors.toList());
    }

    private static SqlColumn buildColumn(MySqlSQLColumnDefinition column) {
        SqlColumn sqlColumn = new SqlColumn();
        sqlColumn.setNameInDb(column.getName().getSimpleName());
        sqlColumn.setNameCamel(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, sqlColumn.getNameInDb()));
        sqlColumn.setComment(column.getComment().toString());
        sqlColumn.setNullable(!hasConstrain(column, NotNullConstraint.class));
        sqlColumn.setPrimaryKey(hasConstrain(column, SQLColumnPrimaryKey.class));
        sqlColumn.setUnique(hasConstrain(column, SQLColumnUniqueKey.class));
        sqlColumn.setDbType(column.getDataType().getName());
        sqlColumn.setJavaType(SqlType.getJavaType(sqlColumn.getDbType()));
        Validate.notNull(sqlColumn.getJavaType() == null, "unknown sql type : " + sqlColumn.getDbType());
        return sqlColumn;
    }


    private static boolean hasConstrain(MySqlSQLColumnDefinition mySqlSQLColumnDefinition, Class<?> clz) {
        return mySqlSQLColumnDefinition.getConstraints().stream().anyMatch(sqlColumnConstraint -> clz.isAssignableFrom(sqlColumnConstraint.getClass()));
    }
}
