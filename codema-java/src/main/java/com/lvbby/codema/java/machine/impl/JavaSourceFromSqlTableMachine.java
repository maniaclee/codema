package com.lvbby.codema.java.machine.impl;

import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaClassUtils;

/**
 * 解析SqlTable
 * @author dushang.lp
 * @version $Id: JavaSourceFromClassFullNameMachine.java, v 0.1 2018年01月19日 下午6:51 dushang.lp Exp $
 */
public class JavaSourceFromSqlTableMachine extends AbstractBaseMachine<SqlTable, JavaClass> {
    @Override
    protected void doCode() throws Exception {
        handle(BasicResult.instance(JavaClassUtils.convert(source)));
    }
}