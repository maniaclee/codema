package com.lvbby.codema.app.repository;

import com.lvbby.codema.java.template.$Class1_;
import com.lvbby.codema.java.template.$Null_;
import com.lvbby.codema.java.template.$TemplateClass_;
import com.lvbby.codema.java.template.annotaion.Sentence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.lvbby.codema.java.template.$Symbols_.$class_;

/**
 * Created by lipeng on 2016/12/24.
 */
@Component
@Sentence("var Repository = @config.parseDestClassName(from);")
public class $Repository_ {
    @Sentence("var TemplateClass = srcClassName;")
    @Sentence("var templateClass = srcClassNameUncapitalized;")
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
    // if (rm.buildReturnMethod !=null){
    // Class1=rm.buildReturnMethod.returnType.name;
    // }
    // %>
    public $Class1_ $invoke_($Null_ $signature_) {

        // <% if (pm !=null){
        // class="from";
        // %>
        // ${pm.returnType} from = ${buildUtilClass.name}.${pm.name}(src);
        // <%}%>

        // <% if (@m.returnVoid()){ %>
        $templateClass_.$invoke_($class_);
        //<%}else if(rm.buildReturnMethod !=null){%>
        //$Class1_ re = ${buildUtilClass.name}.${rm.buildReturnMethod.name}($templateClass_.$invoke_($class_));
        //return re;
        //<%}else{%>
        $Class1_ re = $templateClass_.$invoke_($class_);
        return re;
        //<%}%>
    }
    // <% }%>

}
