package com.lvbby.codema.app.delegate;

import com.lvbby.codema.java.template.*;
import static com.lvbby.codema.java.template.$Symbols_.*;
import com.lvbby.codema.java.template.annotaion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lipeng on 2016/12/24.
 */
@Sentence("var Delegate = destClassName;")
@Sentence("if(strutil.length(interfaces)>0){Delegate = Delegate + ' implements ' + interfaces;}")
@Service
public class $Delegate_{
    @Autowired
    @Sentence("var TemplateClass = srcClassName;")
    @Sentence("var templateClass = srcClassNameUncapitalized;")
    private $TemplateClass_ $templateClass_;

    @Foreach(value = " m in source.methods", body = {
            "var invoke = m.name;",
            "var Class1 = m.returnType;",
            "var class = @m.getArgsInvoke();",
            "var signature = @m.getArgsSignature();"
    })
    public $Class1_ $invoke_($Null_ $signature_) {
        // <% if (@m.returnVoid()){ %>
        $templateClass_.$invoke_($class_);
        //<%}else{%>
        return $templateClass_.$invoke_($class_);
        //<%}%>
    }
}
