package com.lvbby.codema.java.machine.impl;

import com.lvbby.codema.java.machine.AbstractJavaInputMachine;
import com.lvbby.codema.java.result.JavaBasicTemplateResult;

/**
 * 基本的模板machine
 * @author dushang.lp
 * @version $Id: BasicJavaCodeMachine.java, v 0.1 2017年12月15日 下午1:10 dushang.lp Exp $
 */
public class BasicJavaCodeMachine extends AbstractJavaInputMachine<String> {

    public BasicJavaCodeMachine() {
    }

    public BasicJavaCodeMachine(String template) {
        setTemplate(template);
    }

    @Override
    protected void doCode() throws Exception {
        handle(new JavaBasicTemplateResult(this, getTemplate(), getSource()));
    }
}