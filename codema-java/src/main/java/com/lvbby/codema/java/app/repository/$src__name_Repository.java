package com.lvbby.codema.java.app.repository;

import com.lvbby.codema.java.template.$Class1_;
import com.lvbby.codema.java.template.$Null_;
import com.lvbby.codema.java.template.$TemplateClass_;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.lvbby.codema.java.template.$Symbols_.$class_;

/**
 * Created by lipeng on 2016/12/24.
 * 朕心甚慰！！！！
 */
@Component
public class $src__name_Repository {

    @Autowired
    private $TemplateClass_ $templateClass_;

    // <%
    // for( m in src.methods){
    //var invoke = m.name;
    //var Class1 = m.returnType;
    //var class = @m.getArgsInvoke();
    //var signature = m.argsSignature;
    // %>
    @Test
    public $Class1_ $invoke_($Null_ $signature_) throws Exception {

        $Class1_ re = $templateClass_.$invoke_($class_);
        return re;
        //<%}%>
    }
    // <% }%>

}
