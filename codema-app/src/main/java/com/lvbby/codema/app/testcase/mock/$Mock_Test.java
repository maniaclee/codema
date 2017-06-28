package com.lvbby.codema.app.testcase.mock;

import com.lvbby.codema.java.template.$Class1_;
import com.lvbby.codema.java.template.$Class_;
import com.lvbby.codema.java.template.Foreach;
import com.lvbby.codema.java.template.ForeachSub;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.lvbby.codema.java.template.$GenericTypeArg_.$GenericTypeArgInstance_;

/**
 * Created by dushang.lp on 2017/5/24.
 */
public class $Mock_Test {


    // <%
    // for( dm in  methods){
    //      for(d in dm.dependencyMethods){
    //          var Class1 = d.javaField.type.name;
    //          var class1 = d.javaField.name;
    //%>
    @ForeachSub("dm in  methods")
    @ForeachSub(value = "d in dm.dependencyMethods", body = {
            "var Class1 = d.javaField.type.name",
            "var class1 = d.javaField.name"})
    @Mock
    private $Class1_ $class1_;
    //      <%}} %>

    /*# <%
     var Class = src.name;
     var class = @src.getNameCamel();
     %> */
    @InjectMocks
    private $Class_ $class_;

    @Before
    public void init() {
    }

    // <%
    // for( m in  methods){
    //      var method = m.javaMethod.name;
    //      var returnVoid = @m.getJavaMethod().returnVoid();
    //      var deps = @m.parseMockSentence();
    //      var GenericTypeArgInstance = @m.parseMockInvoke();
    // %>
    @Test
    public void $method_Test() throws Exception {
        //<% for(s in deps){
        //%>
        //${s}
        //<%}%>
        //<% if(returnVoid){ %>
        //${GenericTypeArgInstance};
        //<% } else { %>
        @ForeachSub("sdf")
        int a = 3;
        Assert.assertNotNull($GenericTypeArgInstance_);
        //<%}%>
    }
    //<%}%>

    private void success(Object response, Object expect) {
        Assert.assertEquals(response, expect);
    }
}
