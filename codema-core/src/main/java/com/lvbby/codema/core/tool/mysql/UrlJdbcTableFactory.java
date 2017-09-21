package com.lvbby.codema.core.tool.mysql;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.tool.mysql.entity.SqlColumn;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import org.apache.commons.collections.CollectionUtils;

import java.net.URI;
import java.sql.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by peng on 16/7/27.
 */
public class UrlJdbcTableFactory implements JdbcTableFactory {
    Connection conn = null;
    private String jdbcUrl;
    private String user;
    private String password;
    private List<String> tableFilters = Lists.newArrayList();

    public static UrlJdbcTableFactory of(String jdbcUrl, String user, String password) {
        UrlJdbcTableFactory re = new UrlJdbcTableFactory();
        re.setJdbcUrl(jdbcUrl);
        re.setUser(user);
        re.setPassword(password);
        return re;
    }

    public static UrlJdbcTableFactory of(URI uri) {
        UrlJdbcTableFactory re = new UrlJdbcTableFactory();
        re.setJdbcUrl(uri.toString());
        for (Matcher m = Pattern.compile("table=([^&]+)").matcher(uri.toString()); m.find(); )
            re.tableFilters.add(m.group(1));
        return re;
    }


    @Override
    public List<SqlTable> getTables() throws Exception {
        init();
        List<SqlTable> re = Lists.newLinkedList();
        for (String s : getTableNames()) {
            SqlTable instance = SqlTable.instance(s);
            instance.setFields(getFields(instance));
            instance.buildPrimaryKeyField(getPrimaryKey(instance.getNameInDb()));
            re.add(instance);
        }
        return re;
    }

    private String getPrimaryKey(String table) throws SQLException {
        ResultSet pkRSet = conn.getMetaData().getPrimaryKeys(null, null, table);
        while (pkRSet.next()) {
            return pkRSet.getObject(4).toString();
        }
        return null;
    }

    private void init() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection(getJdbcUrl(), getUser(), getPassword());
    }

    private List<String> getTableNames() throws SQLException {
        DatabaseMetaData md = conn.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);
        List<String> re = Lists.newLinkedList();
        while (rs.next()) re.add(rs.getString(3));
        if (!CollectionUtils.isEmpty(tableFilters))
            re = Lists.newArrayList(CollectionUtils.intersection(re, tableFilters));
        return re;
    }

    private List<SqlColumn> getFields(SqlTable SqlTable) throws SQLException {
        List<SqlColumn> re = Lists.newArrayList();
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet rs = meta.getColumns(null, null, SqlTable.getName(), "%");
        while (rs.next()) {
            SqlColumn f = SqlColumn.instance(rs.getString(4));
            //            f.setDbType(rs.getString(5));
            f.setDbType(rs.getString(6));
            f.setJavaType(SqlType.getJavaType(f.getDbType()));
            f.setComment(rs.getString(12));
            // f.setNameInDb(rs.getString("COLUMN_NAME"));
            // f.setDbType(rs.getString("TYPE_NAME"));
            re.add(f);
        }
        return re;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
