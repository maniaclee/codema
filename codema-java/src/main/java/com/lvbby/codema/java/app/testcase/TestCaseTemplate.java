package com.lvbby.codema.java.app.testcase;

import com.alibaba.fastjson.JSON;
import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.core.render.TemplateEngine;
import com.lvbby.codema.core.render.TemplateEngineFactory;
import com.lvbby.codema.java.app.mvn.MavenConfig;
import com.lvbby.codema.java.template.$Class_;
import com.lvbby.codema.java.template.JavaClassParser;
import com.lvbby.codema.java.template.JavaTemplateParser;
import com.lvbby.codema.java.template.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaSrcLoader;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.lvbby.codema.java.template.$Symbols_.re;

/**
 * Created by lipeng on 17/1/5.
 */
public class TestCaseTemplate {

    @Autowired
    private $Class_ $c__nameCamel_;

    //<%
    // for(m in c.methods){
    // if(m.returnType !=null){
    //%>
    @Test
    public void $m__name_(/*# m.loadParameterSignature()  */) {
        // if m.isVoid
        // m.returnType re = m.name(m.loadParameter());
        System.out.println(JSON.toJSONString(re));
    }
    //<%}}%>

    public static void main(String[] args) {
        String template = new JavaTemplateParser().parse(TestCaseTemplate.class);
        System.out.println(template);
        CompilationUnit cu = JavaSrcLoader.getJavaSrcCompilationUnit(MavenConfig.class);
        JavaClass src = new JavaClassParser().parse(cu);
        TemplateEngine templateEngine = TemplateEngineFactory.create(template);
        String result = templateEngine
                .bind("c", src)
                .bind("Class", src.getName())
                .render();
        System.err.println(result);
    }

}
