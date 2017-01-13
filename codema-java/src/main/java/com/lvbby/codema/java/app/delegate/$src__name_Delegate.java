package com.lvbby.codema.java.app.delegate;

import com.lvbby.codema.java.template.$Class1_;
import com.lvbby.codema.java.template.$Null_;
import com.lvbby.codema.java.template.$TemplateClass_;

import static com.lvbby.codema.java.template.$Symbols_.$class_;

/**
 * Created by lipeng on 2016/12/24.
 */
public class $src__name_Delegate {

    private $TemplateClass_ $templateClass_;

    // <%
    // for( m in src.methods){
    //var invoke = m.name;
    //var Class1 = m.returnType;
    //var class = @m.getArgsInvoke();
    //var signature = @m.getArgsSignature();
    // %>
    public $Class1_ $invoke_($Null_ $signature_) throws Exception {
        // <% if (@m.returnVoid()){ %>
        $templateClass_.$invoke_($class_);
        //<%}else{%>
        $Class1_ re = $templateClass_.$invoke_($class_);
        return re;
        //<%}%>
    }
    // <% }%>

}
