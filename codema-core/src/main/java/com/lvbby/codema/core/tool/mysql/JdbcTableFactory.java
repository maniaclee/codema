package com.lvbby.codema.core.tool.mysql;

import com.lvbby.codema.core.tool.mysql.entity.SqlTable;

import java.util.List;

/**
 * Created by dushang.lp on 2017/6/23.
 */
public interface JdbcTableFactory {
    List<SqlTable> getTables() throws Exception;
}
