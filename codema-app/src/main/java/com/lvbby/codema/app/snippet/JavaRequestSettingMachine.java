/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
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