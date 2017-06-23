package com.lvbby.codema.core.tool.mysql;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.tool.mysql.entity.SqlColumn;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.core.utils.ReflectionUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by dushang.lp on 2017/6/23.
 */
public class JavaClassJdbcTableFactory implements JdbcTableFactory {

    private List<SqlTable> tables;

    public static JavaClassJdbcTableFactory of(Class... classes) {
        JavaClassJdbcTableFactory re = new JavaClassJdbcTableFactory();
        if (classes != null) {
            re.tables = Stream.of(classes).map(aClass -> to(aClass)).collect(Collectors.toList());
        }
        return re;
    }

    private static SqlTable to(Class clz) {
        SqlTable instance = SqlTable.instance(clz.getSimpleName());
        instance.setFields(ReflectionUtils.getAllProperties(clz).stream().map(field -> SqlColumn.from(field)).collect(Collectors.toList()));
        return instance;
    }

    @Override
    public List<SqlTable> getTables() throws Exception {
        return tables == null ? Lists.newLinkedList() : tables;
    }
}
