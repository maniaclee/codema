package com.lvbby.codema.java.source;

import com.lvbby.codema.core.source.SourceLoader;
import com.lvbby.codema.core.tool.mysql.SqlParser;
import com.lvbby.codema.java.baisc.JavaSourceParam;
import org.apache.commons.lang3.Validate;

/**
 * Created by lipeng on 2017/1/7.
 */
public class JavaSqlSchemeSourceParser implements SourceLoader<JavaSourceParam> {

    private String sql;

    public JavaSqlSchemeSourceParser(String sql) {
        Validate.notBlank(sql, "create sql can't be blank");
        this.sql = sql;
    }

    @Override
    public JavaSourceParam loadSource() throws Exception {
        return new JavaSourceParam(JavaJdbcUrlSourceParser.convert(SqlParser.fromSql(sql)));
    }
}
