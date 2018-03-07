package com.lvbby.codema.app.delegate;

import com.lvbby.codema.app.AppMachine;
import com.lvbby.codema.app.AppTemplateResource;
import com.lvbby.codema.java.baisc.JavaClassNameParser;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.result.JavaTemplateResult;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by lipeng on 17/1/6.
 */
@AppTemplateResource($Delegate_.class)
public class JavaDelegateMachine extends AppMachine {
    @Getter
    @Setter
    private List<JavaClassNameParser> interfaces;
    /***
     * 如果source是一个interface，实现它
     */
    @Getter
    @Setter
    private boolean detectInterface = false;


    @Override
    public JavaTemplateResult codeEach(JavaClass cu) throws Exception {
        String is = null;
        if (detectInterface && cu.isBeInterface()) {
            is = cu.getName();
        }
        JavaTemplateResult javaTemplateResult = buildJavaTemplateResult();
        if (StringUtils.isNotBlank(is)) {
            javaTemplateResult.bind("interfaces", is);
        }
        return javaTemplateResult;
    }

}
