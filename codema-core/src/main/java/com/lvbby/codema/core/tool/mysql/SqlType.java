package com.lvbby.codema.core.tool.mysql;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

/**
 * Created by lipeng on 2017/1/6.
 */
public class SqlType {
    private static Map<String, Class> map = Maps.newHashMap();

    static {
        map.put("BIT", Boolean.class);
        map.put("BIT", byte[].class);
        map.put("TINYINT", Integer.class);
        map.put("BOOLEAN", Integer.class);
        map.put("SMALLINT", Integer.class);
        map.put("MEDIUMINT", Integer.class);
        map.put("INTEGER", Integer.class);
        map.put("BIGINT", Long.class);
        map.put("FLOAT", Float.class);
        map.put("DOUBLE", Double.class);
        map.put("DECIMAL", BigDecimal.class);
        map.put("DATE", Date.class);
        map.put("DATETIME", Timestamp.class);
        map.put("TIMESTAMP", Timestamp.class);
        map.put("TIME", Time.class);
        map.put("CHAR", String.class);
        map.put("VARCHAR", String.class);
        map.put("BINARY", byte[].class);
        map.put("VARBINARY", byte[].class);
        map.put("TINYBLOB", byte[].class);
        map.put("VARCHAR", String.class);
        map.put("BLOB", byte[].class);
        map.put("VARCHAR", String.class);
        map.put("MEDIUMBLOB", byte[].class);
        map.put("VARCHAR", String.class);
        map.put("LONGBLOB", byte[].class);
        map.put("VARCHAR", String.class);
    }

    public static Class<?> getJavaType(String sqlType) {
        sqlType = StringUtils.trimToNull(sqlType);
        return StringUtils.isBlank(sqlType) ? null : map.get(sqlType.toUpperCase());
    }
}
