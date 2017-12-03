package com.lvbby.codema.app.testcase;

import com.alibaba.fastjson.JSON;
import com.lvbby.codema.java.template.$Class1_;
import static com.lvbby.codema.java.template.$Symbols_.$class_;
import com.lvbby.codema.java.template.$TemplateClass_;
import com.lvbby.codema.java.template.__TemplateUtils_;
import com.lvbby.codema.java.template.annotaion.Foreach;
import com.lvbby.codema.java.template.annotaion.Sentence;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Created by lipeng on 2016/12/24.
 * 朕心甚慰！！！！
 */
@Sentence("var TestCase = destClassName+'Test';")
@RunWith(SpringJUnit4ClassRunner.class)
@EnableAspectJAutoProxy
@ContextConfiguration(classes = { $TestCase_.class})
@ComponentScan("$componentScan_")
public class $TestCase_ {

    @Sentence("var TemplateClass = srcClassName;")
    @Sentence("var templateClass = srcClassNameUncapitalized;")
    @Autowired
    private $TemplateClass_ $templateClass_;

    @Foreach(value = " m in source.methods", body = {
            "var invoke = m.name",
            "var Class1 = m.returnType",
            "var class = @m.getArgsDefaultValue()",
            "var isPrimitive =  @m.returnType.bePrimitive()",
    })
    @Test
    public void $invoke_() throws Exception {
        if (__TemplateUtils_.isFalse("@m.returnVoid()")) {
            $Class1_ re = $templateClass_.$invoke_($class_);
            if (__TemplateUtils_.isTrue("isPrimitive")) {
                __TemplateUtils_.print("assert re > 0 ");
            } else {
                assert re != null;
            }
            println(re);
        } else {
            $templateClass_.$invoke_($class_);
        }
    }

    private void println(Object o){
        System.out.println(JSON.toJSONString(o));
    }

}
