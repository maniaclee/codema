package com.lvbby.codema.app.testcase;

import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaCodemaMachine;
import com.lvbby.codema.java.machine.AbstractJavaInputCodemaMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaTestcaseCodemaMachine extends AbstractJavaInputCodemaMachine {

    @Override
    public Result<JavaClass> codeEach(JavaClass cu){
        return new JavaTemplateResult(this, $TestCase_.class, cu)
//                .bind("springBootConfig", codemaContext.findConfig(JavaSpringBootConfig.class))
                .bind("componentScan",parseComponentScanPackage(getDestPackage()));
    }
    private String parseComponentScanPackage(String pack){
        Matcher matcher = Pattern.compile("^[^\\.]+\\.[^\\.]+").matcher(pack);
        if(matcher.find()){
            return matcher.group();
        }
        return "";
    }

}
