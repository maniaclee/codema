package com.lvbby.codema.core.tool.mysql.machine;

import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.tool.mysql.SqlParser;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;

/**
 *
 * @author dushang.lp
 * @version $Id: SqlSourceFromSqlCreateMachine.java, v 0.1 2018年01月19日 下午7:01 dushang.lp Exp $
 */
public class SqlSourceFromSqlCreateMachine extends AbstractBaseMachine<String, SqlTable> {
    @Override
    protected void doCode() throws Exception {
        //处理多个result
        handleList(SqlParser.fromSql(source));
    }
}