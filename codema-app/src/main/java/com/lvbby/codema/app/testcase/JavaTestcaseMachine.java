package com.lvbby.codema.app.testcase;

import com.lvbby.codema.app.AppMachine;
import com.lvbby.codema.app.AppTemplateResource;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.result.JavaTemplateResult;
import org.apache.commons.lang3.Validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lipeng on 16/12/23.
 */
@AppTemplateResource($TestCase_.class)
public class JavaTestcaseMachine extends AppMachine {

    @Override
    public JavaTemplateResult codeEach(JavaClass cu) throws Exception {
        Validate.notBlank(getDestPackage(),"dest package can't be blank");
        return super.codeEach(cu)
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
