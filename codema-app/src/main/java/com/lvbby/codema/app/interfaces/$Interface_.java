package com.lvbby.codema.app.interfaces;

import com.lvbby.codema.java.template.$Class1_;
import com.lvbby.codema.java.template.$Null_;
import com.lvbby.codema.java.template.annotaion.Sentence;

/**
 * Created by lipeng on 2016/12/24.
 */
@Sentence("var Interface = @config.parseDestClassName(from)+'Service';")
public interface $Interface_ {


    // <%
    // for( m in src.methods){
    //var invoke = m.name;
    //var Class1 = m.returnType;
    //var class = @m.getArgsInvoke();
    //var signature = @m.getArgsSignature();
    // %>
    $Class1_ $invoke_($Null_ $signature_);
    // <% }%>

}
