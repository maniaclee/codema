package com.lvbby.codema.java.app.delegate;

import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.core.render.TemplateEngine;
import com.lvbby.codema.core.render.TemplateEngineFactory;
import com.lvbby.codema.java.app.mvn.MavenConfig;
import com.lvbby.codema.java.template.*;
import com.lvbby.codema.java.template.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaLexer;
import com.lvbby.codema.java.tool.JavaSrcLoader;
import org.junit.Test;

import static com.lvbby.codema.java.template.$Symbols_.$class_;

/**
 * Created by lipeng on 2016/12/24.
 * 朕心甚慰！！！！
 */
public class $c__name_Delegate {

    private $TemplateClass_ $templateClass_;

    // <%
    // for( m in c.methods){
    //var invoke = m.name;
    //var Class1 = m.returnType;
    //var class = @m.getArgsInvoke();
    //var signature = m.argsSignature;
    // %>
    @Test
    public $Class1_ $invoke_($Null_ $signature_) throws Exception {
        // <% if (@m.isVoid()){ %>
        $templateClass_.$invoke_($class_);
        //<%}else{%>
        $Class1_ re = $templateClass_.$invoke_($class_);
        return re;
        //<%}%>
    }
    // <% }%>

    public static void main(String[] args) {
        String template = new JavaSrcTemplateParser().parse($c__name_Delegate.class);
        System.out.println(template);
        CompilationUnit cu = JavaSrcLoader.getJavaSrcCompilationUnit(MavenConfig.class);
        JavaClass src = new JavaClassParser().parse(cu);
        TemplateEngine templateEngine = TemplateEngineFactory.create(template);
        String result = templateEngine
                .bind("c", src)
                .bind("TemplateClass", src.getName())
                .bind("templateClass", JavaLexer.camel(src.getName()))
                .bind("Null", "")
                .render();
        System.err.println(result);
    }

    public static TemplateEngine getJavaSrcTemplateEngine(CompilationUnit cu, Class<?> javaSrcTemplate) {
        String template = new JavaSrcTemplateParser().parse(javaSrcTemplate);
        JavaClass src = new JavaClassParser().parse(cu);
        TemplateEngine templateEngine = TemplateEngineFactory.create(template);
        return templateEngine
                .bind("c", src)
                .bind("TemplateClass", src.getName())
                .bind("templateClass", JavaLexer.camel(src.getName()))
                .bind("Null", "");
    }
}
