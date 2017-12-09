/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.lvbby.codema.app.javaMdDoc;

import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.java.machine.JavaClassMachineFactory;

/**
 *
 * @author dushang.lp
 * @version $Id: JavaMdDocEasyMachine.java, v 0.1 2017年12月09日 下午1:05 dushang.lp Exp $
 */
public class JavaMdDocEasyMachine extends AbstractBaseMachine<String,String>{
    @Override protected void doCode() throws Exception {
        String service;
        String method;
        if(source.matches(".*(\\.[a-z][^\\.]+)$")){
            int i = source.lastIndexOf(".");
            service=source.substring(0,i);
            method=source.substring(i+1);
        }else {
            String[] split = source.split("[#]");
            service = split[0];
            method = split.length > 1 ? split[1] : null;
        }
        setResult(BasicResult.instance(service));
        //link到JavaMdDocMachine
        JavaMdDocMachine mdDocMachine = new JavaMdDocMachine();
        mdDocMachine.setMethod(method);
        nextWithCheck(JavaClassMachineFactory.fromClassFullName().next(mdDocMachine));
    }
}