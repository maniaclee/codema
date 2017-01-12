package com.lvbby.codema.java.app.repository;

import com.lvbby.codema.java.template.$Class1_;
import com.lvbby.codema.java.template.$Null_;
import com.lvbby.codema.java.template.$TemplateClass_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.lvbby.codema.java.template.$Symbols_.$class_;

/**
 * Created by lipeng on 2016/12/24.
 */
@Component
public class $src__name_Repository {

    @Autowired
    private $TemplateClass_ $templateClass_;


    // <%
    // for( rm in  methods){
    // var m = rm.javaMethod;
    //var invoke = m.name;
    //var Class1 = m.returnType.name;
    //var class = @m.getArgsInvoke();
    //var signature = @m.getArgsSignature();
    // var pm = @rm.getBuildParameterMethods().isEmpty()?null:@rm.getBuildParameterMethods().get(0);

    // %>
    public $Class1_ $invoke_($Null_ $signature_) throws Exception {

        // <% if (pm !=null){
        // class="from";
        // %>
        // ${pm.returnType} from = BuildUtils.${pm.name}(src);
        // <%}%>

        // <% if (@m.isVoid()){ %>
        $templateClass_.$invoke_($class_);
        //<%}else{%>
        $Class1_ re = $templateClass_.$invoke_($class_);
        return re;
        //<%}%>
    }
    // <% }%>

}
