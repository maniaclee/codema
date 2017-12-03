package com.lvbby.codema.app.testcase;

import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaInputMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;
import org.apache.commons.lang3.Validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaTestcaseMachine extends AbstractJavaInputMachine {

    @Override
    public Result<JavaClass> codeEach(JavaClass cu){
        Validate.notBlank(getDestPackage(),"dest package can't be blank");
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
