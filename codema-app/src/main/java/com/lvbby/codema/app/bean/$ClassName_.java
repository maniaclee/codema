package com.lvbby.codema.app.bean;

import com.lvbby.codema.java.template.$Class1_;
import com.lvbby.codema.java.template.annotaion.Foreach;
import com.lvbby.codema.java.template.annotaion.Sentence;

import java.io.Serializable;

/**
 * Created by lipeng on 2016/12/24.
 */
@Sentence("var ClassName  = destClassName;")
public class $ClassName_ implements Serializable {
    private static final long serialVersionUID = -1;
    @Foreach(value = "f in source.fields", body = {
            "var Class1 = f.type",
            "var class1 = f.name"

    })
    private $Class1_ $class1_;

    // <%
    // for( f in source.fields){
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
