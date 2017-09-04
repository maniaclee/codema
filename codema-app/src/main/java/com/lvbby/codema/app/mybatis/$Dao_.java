package com.lvbby.codema.app.mybatis;

import com.lvbby.codema.java.template.$Class1_;
import com.lvbby.codema.java.template.$Class_;
import com.lvbby.codema.java.template.annotaion.Sentence;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by lipeng on 2016/12/24.
 */
@Sentence("var Dao = destClassName;")
public interface $Dao_ {

    //<%
    // var id = table.primaryKeyField;
    // var Class = from.name;
    // var class=lee.unCapital(from.name);
    // var Class1=id.javaTypeName;
    // %>
    long insert(@Param("entity") $Class_ $class_);

    void inserts(@Param("entity") List<$Class_> $class_s);

    @Select("select * from ${table.nameInDb} where ${id.nameInDb} = #{id}")
    $Class_ queryById(@Param("id") $Class1_ id);

    List<$Class_> queryByIds(List<$Class1_> ids);
    //<%%>
}