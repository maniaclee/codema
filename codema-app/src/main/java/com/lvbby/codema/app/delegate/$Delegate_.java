package com.lvbby.codema.app.delegate;

import com.lvbby.codema.java.template.*;
import static com.lvbby.codema.java.template.$Symbols_.*;
import com.lvbby.codema.java.template.annotaion.*;

/**
 * Created by lipeng on 2016/12/24.
 */
@Sentence("var Delegate = @config.parseDestClassName(from);")
@Sentence("var Interface0 = @config.findImplementInterfacesAsString(from);")
public class $Delegate_  implements $Interface0_ {
    @Sentence("var TemplateClass = srcClassName;")
    @Sentence("var templateClass = srcClassNameUncapitalized;")
    private $TemplateClass_ $templateClass_;

    @Foreach(value = " m in from.methods", body = {
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
