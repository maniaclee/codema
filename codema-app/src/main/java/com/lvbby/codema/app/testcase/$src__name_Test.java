package com.lvbby.codema.app.testcase;

import com.lvbby.codema.java.template.$TemplateClass_;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//<% if(springBootConfig!=null){ %>
// <%}%>


/**
 * Created by lipeng on 2016/12/24.
 * 朕心甚慰！！！！
 */
//<% if(springBootConfig!=null){ %>
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootApplication
@EnableAspectJAutoProxy
@ContextConfiguration(classes = {$src__name_Test.class})
// <%}%>
public class $src__name_Test {

    @Autowired
    private $TemplateClass_ $templateClass_;

    // <%
    // for( m in src.methods){
    //      var invoke = m.name;
    //      var Class1 = m.returnType;
    //      var class = @m.getArgsDefaultValue();
    //      var isPrimitive = @Class1.bePrimitive();
    // %>
    @Test
    public void $invoke_() throws Exception {
        //${class}

    }
    // <%}%>

}
