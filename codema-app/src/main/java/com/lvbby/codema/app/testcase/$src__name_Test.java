package com.lvbby.codema.app.testcase;

import com.alibaba.fastjson.JSON;
import com.lvbby.codema.java.template.$Class1_;
import com.lvbby.codema.java.template.$TemplateClass_;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.lvbby.codema.java.template.$Symbols_.$class_;

/**
 * Created by lipeng on 2016/12/24.
 * 朕心甚慰！！！！
 */
public class $src__name_Test {

    @Autowired
    private $TemplateClass_ $templateClass_;

    // <%
    // for( m in src.methods){
    //var invoke = m.name;
    //var Class1 = m.returnType;
    //var class = @m.getArgsDefaultValue();
    // %>
    @Test
    public void $invoke_() throws Exception {
        // <% if (!@m.returnVoid()){ %>
        $Class1_ re = $templateClass_.$invoke_($class_);
        assert re != null;
        System.out.println(JSON.toJSONString(re));
        //<%}else{%>
        $templateClass_.$invoke_($class_);
        //<%}%>

    }
    // <% }%>

}