package com.lvbby.codema.java.machine.impl;

import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaDuplexMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;

/**
 * 模板是Java模板
 * @author dushang.lp
 * @version $Id: JavaTemplateMachine.java, v 0.1 2018年03月07日 下午3:00 dushang.lp Exp $
 */
public class JavaTemplateMachine extends AbstractJavaDuplexMachine {
    public JavaTemplateMachine() {
    }

    public JavaTemplateMachine(String template) {
        setTemplate(template);
    }

    @Override
    public JavaTemplateResult codeEach(JavaClass cu) throws Exception {
        return buildJavaTemplateResult();
    }
}