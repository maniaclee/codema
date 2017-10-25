package com.lvbby.codema.core.tool.mysql.entity;

import com.google.common.base.CaseFormat;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.lvbby.codema.core.utils.CaseFormatUtils;
import org.apache.commons.lang3.StringUtils;
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
        table=table.replace("`","");
        SqlTable sqlTable = new SqlTable();
        sqlTable.setName(CaseFormatUtils.toCaseFormat(CaseFormat.UPPER_CAMEL,table));
        sqlTable.setNameInDb(CaseFormatUtils.toCaseFormat(CaseFormat.LOWER_UNDERSCORE,table));
        return sqlTable;
    }


    public void buildPrimaryKeyField(String primaryKeyColumn) {
        if(StringUtils.isBlank(primaryKeyColumn))
            return;
        Function<String,String> format=input -> input.replaceAll("-|_","").toLowerCase();
        this.primaryKeyField = fields.stream().filter(f -> Objects.equals(format.apply(f.getNameInDb()), format.apply(primaryKeyColumn))).findFirst().orElse(null);
        if(this.primaryKeyField!=null){
            primaryKeyField.setPrimaryKey(true);
        }
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
