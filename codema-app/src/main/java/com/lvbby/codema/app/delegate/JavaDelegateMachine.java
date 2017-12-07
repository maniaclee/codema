package com.lvbby.codema.app.delegate;

import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.java.baisc.JavaClassNameParser;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaInputMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 17/1/6.
 */
public class JavaDelegateMachine extends AbstractJavaInputMachine {
    private List<JavaClassNameParser>  interfaces;
    /***
     * 如果source是一个interface，实现它
     */
    private boolean detectInterface = false;


    @Override public Result<JavaClass> codeEach(JavaClass cu) throws Exception {
        JavaTemplateResult javaTemplateResult = new JavaTemplateResult(this, $Delegate_.class, cu);
        if(CollectionUtils.isNotEmpty(interfaces)){
         javaTemplateResult
                .bind("interfaces",interfaces.stream().map(javaClassNameParser -> javaClassNameParser.getClassName(cu))
                        .collect(Collectors.joining(",")));
        }
         return javaTemplateResult;
    }

    /**
     * Getter method for property   interfaces.
     *
     * @return property value of interfaces
     */
    public List<JavaClassNameParser> getInterfaces() {
        return interfaces;
    }

    /**
     * Setter method for property   interfaces .
     *
     * @param interfaces  value to be assigned to property interfaces
     */
    public void setInterfaces(List<JavaClassNameParser> interfaces) {
        this.interfaces = interfaces;
    }
}
