package com.lvbby.codema.app.testcase.mock;

import com.lvbby.codema.java.template.$Class1_;
import com.lvbby.codema.java.template.$Class_;
import com.lvbby.codema.java.template.$GenericTypeArg_;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static com.lvbby.codema.java.template.$GenericTypeArg_.$GenericTypeArgInstance_;

/**
 * Created by dushang.lp on 2017/5/24.
 */
public class $Mock_Test {

    /*# <%
     var Class1 = src.name;
     var class1 = @src.getNameCamel();
     %> */
    @Mock
    private $Class1_ $class1_;
    /*# <%
     var Class = src.name;
     var class = @src.getNameCamel();
     %> */
    @InjectMocks
    private $Class_ $class_;

    @Before
    public void init() {
        /*#
        <%
        var GenericTypeArg = "Shit";
        var GenericTypeArgInstance = "GenericTypeArgInstance";

          %>*/
        $GenericTypeArg_ value = new $GenericTypeArg_();
        Mockito.when(
                $class1_.$method_(Mockito.any($GenericTypeArg_.class)))
                .thenReturn(value);
    }

    // <%
    // for( m in src.methods){
    //      var method = m.name;
    //      var isPrimitive =  @m.returnType.bePrimitive();
    //      var GenericTypeArg = "Shit";
    //      var GenericTypeArgInstance = "GenericTypeArgInstance";
    // %>
    @Test
    public void $method_Test() throws Exception {


        $GenericTypeArg_ re = $class_.$method_($GenericTypeArgInstance_);
        success(re, null);
    }
    //<%}%>

    private void success(Object response, Object expect) {
        Assert.assertEquals(response, expect);
    }
}
