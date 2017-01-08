package com.lvbby.codema.java.app.mybatis;

import com.lvbby.codema.java.template.$Class1_;
import com.lvbby.codema.java.template.$Class_;

import java.util.List;

/**
 * Created by lipeng on 2016/12/24.
 */
public interface $src__name_Dao {

    //<%
    // var id = from.primaryKeyField;
    // var Class = src.name;
    // var class=lee.unCapital(src.name);
    // var Class1=@id.getJavaTypeString();
    // %>
    void insert($Class_ $class_);

    void inserts(List<$Class_> $class_s);

    $Class_ queryById($Class1_ id);

    List<$Class_> queryByIds(List<$Class1_> ids);
    //<%%>
}
