package com.lvbby.codema.core.tool.mysql;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.Objects;


/**
 * Created by peng on 16/7/27.
 */
public class SqlTable {
    /**
     * Capital camel name like UserDetail
     */
    String name;
    String nameInDb;
    List<SqlColumn> fields = Lists.newLinkedList();
    private SqlColumn primaryKeyField;

    public static SqlTable instance(String table) {
        SqlTable sqlTable = new SqlTable();
        sqlTable.setNameInDb(table);
        return sqlTable;
    }


    public void buildPrimaryKeyField(String primaryKeyColumn) {
        this.primaryKeyField = fields.stream().filter(f -> Objects.equals(f.getNameInDb(), primaryKeyColumn)).findFirst().orElse(null);
    }

    public SqlColumn getPrimaryKeyField() {
        return primaryKeyField;
    }

    public void setPrimaryKeyField(SqlColumn primaryKeyField) {
        this.primaryKeyField = primaryKeyField;
    }

    public String getNameInDb() {
        return nameInDb;
    }

    public void setNameInDb(String nameInDb) {
        this.nameInDb = nameInDb;
    }

    public String getName() {
        return name;
    }

    public void setName(String tableName) {
        this.name = tableName;
    }

    public List<SqlColumn> getFields() {
        return fields;
    }

    public void setFields(List<SqlColumn> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
