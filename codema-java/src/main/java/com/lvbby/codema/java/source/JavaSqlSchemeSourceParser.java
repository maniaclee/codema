package com.lvbby.codema.java.source;

import com.lvbby.codema.core.SourceParser;
import com.lvbby.codema.core.tool.mysql.SqlParser;
import com.lvbby.codema.java.app.baisc.JavaSourceParam;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.net.URI;

/**
 * Created by lipeng on 2017/1/7.
 */
public class JavaSqlSchemeSourceParser implements SourceParser<JavaSourceParam> {
    @Override
    public String getSupportedUriScheme() {
        return "file://sql/";
    }

    @Override
    public JavaSourceParam parse(URI from) throws Exception {
        return new JavaSourceParam(JavaJdbcUrlSourceParser.convert(SqlParser.fromSql(IOUtils.toString(new FileInputStream(from.getPath())))));
    }

}
