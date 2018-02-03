package com.lvbby.codema.app.snippet;

/**
 *
 * @author dushang.lp
 * @version $Id: JavaRequestSettingMachine.java, v 0.1 2017年12月18日 下午9:10 dushang.lp Exp $
 */
public class JavaRequestSettingMachine extends BasicJavaCodeMachine {

    public JavaRequestSettingMachine() {
        setTemplate(loadResourceAsString("RequestSetting"));
    }
}