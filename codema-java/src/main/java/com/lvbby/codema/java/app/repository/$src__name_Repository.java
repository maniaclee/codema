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
    // for( wm in  methods){
    //var m = wm.javaMethod;
    //var invoke = m.name;
    //var class = @m.getArgsInvoke();
    //var signature = m.argsSignature;
    //var Class1 = m.returnType;

    //var returnMethod = wm.buildReturnMethod;
    //var parameterMethod = wm.buildParameterMethod;

    // if (returnMethod!=null){
    //  Class1 = returnMethod.returnType
    // }
    // %>
    @Test
    public $Class1_ $invoke_($Null_ $signature_) throws Exception {
        $templateClass_.$invoke_($class_);
        $Class1_ re = $templateClass_.$invoke_($class_);
        return re;
        //<%}%>
    }
    // <% }%>

}
