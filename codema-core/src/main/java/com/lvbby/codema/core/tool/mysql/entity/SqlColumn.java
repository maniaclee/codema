package com.lvbby.codema.core.tool.mysql.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Created by peng on 16/7/27.
 */
public class SqlColumn {
    private String nameCamel;
    private String nameInDb;
    private String dbType;
    private Class<?> javaType;
    private String comment;
    private boolean primaryKey = false;
    private boolean nullable = true;
    private boolean hasIndex = false;
    private boolean unique = false;

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public String getNameCamel() {
        return nameCamel;
    }

    public void setNameCamel(String nameCamel) {
        this.nameCamel = nameCamel;
    }

    public String getNameInDb() {
        return nameInDb;
    }

    public void setNameInDb(String nameInDb) {
        this.nameInDb = nameInDb;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isHasIndex() {
        return hasIndex;
    }

    public void setHasIndex(boolean hasIndex) {
        this.hasIndex = hasIndex;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
