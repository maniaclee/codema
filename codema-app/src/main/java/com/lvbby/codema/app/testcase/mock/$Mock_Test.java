package com.lvbby.codema.app.testcase.mock;

import com.lvbby.codema.java.template.$Class1_;
import com.lvbby.codema.java.template.$Class_;
import com.lvbby.codema.java.template.annotaion.Foreach;
import com.lvbby.codema.java.template.annotaion.Sentence;
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


    @Foreach("dm in  methods")
    @Foreach(value = "d in dm.dependencyMethods", body = {
            "var Class1 = d.javaField.type.name",
            "var class1 = d.javaField.name"})
    @Mock
    private $Class1_ $class1_;

    @Sentence("var Class = src.name;")
    @Sentence("var class = @src.getNameCamel();")
    @InjectMocks
    private $Class_ $class_;

    @Before
    public void init() {
    }

    @Foreach(value = "m in methods", body = {
            "var method = m.javaMethod.name",
            "var returnVoid = @m.getJavaMethod().returnVoid()",
            "var deps = @m.parseMockSentence()",
            "var GenericTypeArgInstance = @m.parseMockInvoke()"
    })
    @Test
    public void $method_Test() throws Exception {
        //<% for(s in deps){ %>
        //${s}
        //<%}%>
        //<% if(returnVoid){ %>
        //${GenericTypeArgInstance};
        //<% } else { %>
        Assert.assertNotNull($GenericTypeArgInstance_);
        //<%}%>
    }

    private void success(Object response, Object expect) {
        Assert.assertEquals(response, expect);
    }
}
