package com.lvbby.codema.java.app.dto;

import com.lvbby.codema.java.template.$Class1_;

import java.io.Serializable;

/**
 * Created by lipeng on 2016/12/24.
 */
public class $src__name_DTO implements Serializable {


    // <% for( f in src.fields){
    // var Class1 = f.type;
    // var class1 = f.name;
    // %>
    private $Class1_ $class1_;
    // <% }%>

    // <%
    // for( f in src.fields){
    // var Class1 = f.type;
    // var class1 = f.name;
    // var capital = lee.capital(f.name);
    // %>
    public $Class1_ get$capital_() {
        return $class1_;
    }

    public void set$capital_($Class1_ $class1_) {
        this.$class1_ = $class1_;
    }

    // <% }%>

}
