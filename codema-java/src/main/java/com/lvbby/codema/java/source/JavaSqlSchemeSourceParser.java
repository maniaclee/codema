package com.lvbby.codema.java.source;

import com.lvbby.codema.core.source.AbstractSourceLoader;
import com.lvbby.codema.core.tool.mysql.SqlParser;
import com.lvbby.codema.java.entity.JavaClass;
import org.apache.commons.lang3.Validate;

/**
 * Created by lipeng on 2017/1/7.
 */
public class JavaSqlSchemeSourceParser extends AbstractSourceLoader<JavaClass> {

    public JavaSqlSchemeSourceParser(String sql) {
        Validate.notBlank(sql, "create sql can't be blank");
        setSource(JavaDbSourceParser.convert(SqlParser.fromSql(sql)));
    }

}
