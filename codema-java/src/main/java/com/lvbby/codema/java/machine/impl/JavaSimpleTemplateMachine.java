package com.lvbby.codema.java.machine.impl;

import com.lvbby.codema.core.AbstractTemplateMachine;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.result.JavaBasicTemplateResult;

/**
 * 模板是普通模板
 * JavaClass --> String
 * @author dushang.lp
 * @version $Id: BasicJavaCodeMachine.java, v 0.1 2017年12月15日 下午1:10 dushang.lp Exp $
 */
public class JavaSimpleTemplateMachine extends AbstractTemplateMachine<JavaClass,String> {

    public JavaSimpleTemplateMachine() {
    }

    public JavaSimpleTemplateMachine(String template) {
        setTemplate(template);
    }

    @Override
    protected void doCode() throws Exception {
        handle(new JavaBasicTemplateResult(this, getTemplate(), getSource()));
    }
}