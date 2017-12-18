package com.lvbby.codema.app.mysql;

/**
 * 产生update的mybatis语句
 * @author dushang.lp
 * @version $Id: MysqlSchemaCodemaConfig.java, v 0.1 2017-09-13  dushang.lp Exp $
 */
public class SqlUpdateMachine extends BaseSqlMachine {

    {
        setTemplate(loadResourceAsString("update.sql"));
    }

}