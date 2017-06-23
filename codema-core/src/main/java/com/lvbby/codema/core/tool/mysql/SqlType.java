package com.lvbby.codema.core.tool.mysql;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by lipeng on 2017/1/6.
 */
public class SqlType {
    private static Map<String, Class> jdbc2javaMap = Maps.newHashMap();
    private static Map<Class, String> java2jdbcMap = Maps.newHashMap();

    static {
        jdbc2javaMap.put("BIT", Boolean.class);

        jdbc2javaMap.put("TINYINT", Integer.class);
        jdbc2javaMap.put("BOOLEAN", Integer.class);
        jdbc2javaMap.put("SMALLINT", Integer.class);
        jdbc2javaMap.put("MEDIUMINT", Integer.class);
        jdbc2javaMap.put("INTEGER", Integer.class);
        jdbc2javaMap.put("INT", Integer.class);

        jdbc2javaMap.put("BIGINT", Long.class);
        jdbc2javaMap.put("FLOAT", Float.class);
        jdbc2javaMap.put("DOUBLE", Double.class);
        jdbc2javaMap.put("DECIMAL", BigDecimal.class);

        jdbc2javaMap.put("DATE", Date.class);
        jdbc2javaMap.put("DATETIME", Date.class);
        jdbc2javaMap.put("TIMESTAMP", Date.class);
        jdbc2javaMap.put("TIME", Time.class);


        jdbc2javaMap.put("BINARY", byte[].class);
        jdbc2javaMap.put("VARBINARY", byte[].class);
        jdbc2javaMap.put("TINYBLOB", byte[].class);
        jdbc2javaMap.put("BLOB", byte[].class);
        jdbc2javaMap.put("MEDIUMBLOB", byte[].class);
        jdbc2javaMap.put("LONGBLOB", byte[].class);

        jdbc2javaMap.put("VARCHAR", String.class);
        jdbc2javaMap.put("CHAR", String.class);
        jdbc2javaMap.put("TEXT", String.class);


        java2jdbcMap.put(Integer.class, "INT");
        java2jdbcMap.put(Long.class, "BIGINT");
        java2jdbcMap.put(Float.class, "FLOAT");
        java2jdbcMap.put(Double.class, "DOUBLE");
        java2jdbcMap.put(BigDecimal.class, "DECIMAL");
        java2jdbcMap.put(Date.class, "DATETIME");
        java2jdbcMap.put(String.class, "VARCHAR");


    }

    public static Class<?> getJavaType(String sqlType) {
        sqlType = StringUtils.trimToNull(sqlType);
        return StringUtils.isBlank(sqlType) ? null : jdbc2javaMap.get(sqlType.toUpperCase());
    }

    public static String getJdbcType(Class clz) {
        return getJdbcType(clz, null);
    }

    public static String getJdbcType(Class clz, Function<String, String> function) {
        String s = java2jdbcMap.get(clz);
        if (StringUtils.isBlank(s))
            return null;
        return function == null ? s : function.apply(s);
    }
}
