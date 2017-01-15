package com.lvbby.codema.java.app.mybatis;

import com.lvbby.codema.java.template.$Class1_;
import com.lvbby.codema.java.template.$Class_;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by lipeng on 2016/12/24.
 */
public interface $src__name_Dao {

    //<%
    // var id = table.primaryKeyField;
    // var Class = src.name;
    // var class=lee.unCapital(src.name);
    // var Class1=id.javaTypeName;
    // %>
    long insert(@Param("entity") $Class_ $class_);

    void inserts(@Param("entity") List<$Class_> $class_s);

    @Select("select * from ${table.nameInDb} where ${id.nameInDb} = #{id}")
    $Class_ queryById(@Param("id") $Class1_ id);

    List<$Class_> queryByIds(List<$Class1_> ids);
    //<%%>
}