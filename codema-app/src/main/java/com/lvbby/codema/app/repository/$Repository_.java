package com.lvbby.codema.app.repository;

import com.lvbby.codema.java.template.$Class1_;
import static com.lvbby.codema.java.template.$Class1_.$class1Object_;
import static com.lvbby.codema.java.template.$Class2_.$class2Object_;
import com.lvbby.codema.java.template.$Null_;
import com.lvbby.codema.java.template.$TemplateClass_;
import com.lvbby.codema.java.template.__TemplateUtils_;
import com.lvbby.codema.java.template.annotaion.Foreach;
import com.lvbby.codema.java.template.annotaion.Sentence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.lvbby.codema.java.template.$Symbols_.$class_;

/**
 * Created by lipeng on 2016/12/24.
 */
@Component
@Sentence("var Repository = destClassName;")
public class $Repository_ {
    @Sentence("var TemplateClass = srcClassName;")
    @Sentence("var templateClass = srcClassNameUncapitalized;")
    @Autowired
    private $TemplateClass_ $templateClass_;


    @Foreach(value = "rm in  methods", body = { " var m = rm.javaMethod;",
                                                "var signature = @m.getArgsSignature();",
                                                "var invoke = m.name;",
                                                "var Class1 = m.returnType.name;",
                                                "var class = @m.getArgsInvoke();",
                                                " var pm = @rm.getBuildParameterMethods().isEmpty()?null:@rm.getBuildParameterMethods().get(0);",
                                                " if (rm.buildReturnMethod !=null){",
                                                "      Class1=rm.buildReturnMethod.returnType.name;",
                                                " }", })
    public $Class1_ $invoke_($Null_ $signature_) {
        /*#
        <%
         var class2Object = null;
         var isCollection = false;
         if(@m.hasParameter() && @m.getArgs().size()==1){
              class2Object = @m.getArgs().get(0).getName();
              isCollection = @m.getArgs().get(0).getType().isCollectionType();
        }
         var class1Object = '';
         if(!@m.returnVoid()){
              class1Object = @m.getReturnType().defaultNullValueAsString();
         }
        %>
        */
        if (__TemplateUtils_.isTrue("class2Object!=null")) {
            if (__TemplateUtils_.isTrue("isCollection")) {
                if ($class2Object_ == null || $class2Object_.isEmpty()) {
                    return $class1Object_;
                }
            } else {
                if ($class2Object_ == null) {
                    return $class1Object_;
                }
            }
        }
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

}
