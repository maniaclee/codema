package com.lvbby.codema.java.source;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.SourceParser;
import com.lvbby.codema.core.tool.mysql.UrlJdbcTableFactory;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.java.baisc.JavaSourceParam;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaField;
import com.lvbby.codema.java.entity.JavaType;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2017/1/7.
 */
public class JavaJdbcUrlSourceParser implements SourceParser<JavaSourceParam> {
    @Override
    public String getSupportedUriScheme() {
        return "jdbc:mysql://";
    }

    @Override
    public JavaSourceParam parse(URI from) throws Exception {
        return new JavaSourceParam(convert(UrlJdbcTableFactory.of(from).getTables()));
    }

    public static List<JavaClass> convert(List<SqlTable> tables) {
        if (tables == null)
            return Lists.newArrayList();
        return tables.stream().map(sqlTable -> {
            JavaClass javaClass = new JavaClass();
            javaClass.setPack("");
            javaClass.setName(sqlTable.getName());
            javaClass.setFields(sqlTable.getFields().stream().map(sqlColumn -> {
                JavaField javaField = new JavaField();
                javaField.setName(sqlColumn.getNameCamel());
                javaField.setType(JavaType.ofClass(sqlColumn.getJavaType()));
                javaField.setProperty(true);
                return javaField;
            }).collect(Collectors.toList()));
            javaClass.setFrom(sqlTable);
            return javaClass;
        }).collect(Collectors.toList());
    }
}
