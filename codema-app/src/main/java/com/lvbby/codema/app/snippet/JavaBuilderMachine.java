package com.lvbby.codema.app.snippet;

import com.lvbby.codema.java.machine.impl.JavaSimpleTemplateMachine;

/**
 *
 * @author dushang.lp
 * @version $Id: JavaRequestSettingMachine.java, v 0.1 2017年12月18日 下午9:10 dushang.lp Exp $
 */
public class JavaBuilderMachine extends JavaSimpleTemplateMachine {

    public JavaBuilderMachine() {
        setTemplate(loadResourceAsString("Builder"));
    }
}