package com.lvbby.codema.app.testcase.mock;

import com.lvbby.codema.java.template.$Class1_;
import com.lvbby.codema.java.template.$Class_;
import com.lvbby.codema.java.template.$GenericTypeArg1_;
import com.lvbby.codema.java.template.$GenericTypeArg_;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static com.lvbby.codema.java.template.$GenericTypeArg_.$GenericTypeArgInstance_;
import static com.lvbby.codema.java.template.$Class2_.$class2Object_;

/**
 * Created by dushang.lp on 2017/5/24.
 */
public class $Mock_Test {

    /*#
     <%
     for(f in injectFields){
       var Class1 = f.type.name;
       var class1 = lee.unCapital(f.type.name);
     %>
    */
    @Mock
    private $Class1_ $class1_;

    //<%}%>

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
         for(f in injectFields){
            var GenericTypeArg1 = "Shit";
            var GenericTypeArgInstance = "GenericTypeArgInstance";
            var class2Object = "a";

          %>*/
        $GenericTypeArg1_ value = null;
        value = new $GenericTypeArg1_();
        Mockito.when(
                $class2Object_.$method_(Mockito.any($GenericTypeArg1_.class)))
                .thenReturn(value);
        //<%}%>
    }

    // <%
    // for( m in src.methods){
    //      var method1 = m.name;
    //      var isPrimitive =  @m.returnType.bePrimitive();
    //      var GenericTypeArg = "Shit";
    //      var GenericTypeArgInstance = "GenericTypeArgInstance";
    // %>
    @Test
    public void $method_Test() throws Exception {


        $GenericTypeArg_ re = $class_.$method1_($GenericTypeArgInstance_);
        success(re, null);
    }
    //<%}%>

    private void success(Object response, Object expect) {
        Assert.assertEquals(response, expect);
    }
}
