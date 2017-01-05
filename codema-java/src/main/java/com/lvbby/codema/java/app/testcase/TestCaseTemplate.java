package com.lvbby.codema.java.app.testcase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lvbby.codema.java.template.$Class_;
import com.lvbby.codema.java.template.$Foreach;
import com.lvbby.codema.java.template.$GenericTypeArg1_;
import com.lvbby.codema.java.template.entity.JavaTemplateParser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.lvbby.codema.java.template.$Symbols_.$Var1_;

/**
 * Created by lipeng on 17/1/5.
 */
public class TestCaseTemplate {

    @Autowired
    private $Class_ $m_instance_;

    /***#
     <%
     for(m in c.methods){
     if(m.returnType !=null){
     #*/
    //<%
    // for(m in c.methods){
    // if(m.returnType !=null){
    @$Foreach("for(m in c.methods)")
    @Test
    public void $m_(/* m.loadParameterSignature()  */) {
        // if m.isVoid
        // m.returnType re = m.name(m.loadParameter());
        // return re;
        $GenericTypeArg1_ re = $m_instance_.$Method_($Var1_);
        System.out.println(JSON.toJSONString(re, SerializerFeature.PrettyFormat));
    }
    //%>

    public static void main(String[] args) {
        System.out.println(new JavaTemplateParser().parse(TestCaseTemplate.class));
    }

}
