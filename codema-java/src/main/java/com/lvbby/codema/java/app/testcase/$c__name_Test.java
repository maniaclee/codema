package com.lvbby.codema.java.app.testcase;

import com.alibaba.fastjson.JSON;
import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.core.render.TemplateEngine;
import com.lvbby.codema.core.render.TemplateEngineFactory;
import com.lvbby.codema.java.app.mvn.MavenConfig;
import com.lvbby.codema.java.template.$Class1_;
import com.lvbby.codema.java.template.$TemplateClass_;
import com.lvbby.codema.java.template.JavaClassParser;
import com.lvbby.codema.java.template.JavaSrcTemplateParser;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaLexer;
import com.lvbby.codema.java.tool.JavaSrcLoader;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.lvbby.codema.java.template.$Symbols_.$class_;

/**
 * Created by lipeng on 2016/12/24.
 * 朕心甚慰！！！！
 */
public class $c__name_Test {

    @Autowired
    private $TemplateClass_ $templateClass_;

    // <%
    // for( m in c.methods){
    //var invoke = m.name;
    //var Class1 = m.returnType;
    //var class = @m.getArgsDefaultValue();
    // %>
    @Test
    public void $invoke_Test() throws Exception {
        // <% if (!@m.isVoid()){ %>
        $Class1_ re = $templateClass_.$invoke_($class_);
        assert re != null;
        System.out.println(JSON.toJSONString(re));
        //<%}else{%>
        $templateClass_.$invoke_($class_);
        //<%}%>

    }
    // <% }%>

    public static void main(String[] args) {
        String template = new JavaSrcTemplateParser().parse($c__name_Test.class);
        System.out.println(template);
        CompilationUnit cu = JavaSrcLoader.getJavaSrcCompilationUnit(MavenConfig.class);
        JavaClass src = new JavaClassParser().parse(cu);
        TemplateEngine templateEngine = TemplateEngineFactory.create(template);
        String result = templateEngine
                .bind("c", src)
                .bind("TemplateClass", src.getName())
                .bind("templateClass", JavaLexer.camel(src.getName()))
                .render();
        System.err.println(result);
    }
}
