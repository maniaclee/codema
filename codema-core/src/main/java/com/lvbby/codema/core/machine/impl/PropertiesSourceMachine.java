package com.lvbby.codema.core.machine.impl;

import com.alibaba.fastjson.JSONObject;
import com.lvbby.codema.core.AbstractBaseMachine;

import java.io.StringReader;
import java.util.Properties;

/**
 * properties
 * @author dushang.lp
 * @version $Id: JsonDataSourceMachine.java, v 0.1 2018年01月24日 下午6:14 dushang.lp Exp $
 */
public class PropertiesSourceMachine extends AbstractBaseMachine<String, JSONObject> {
    @Override
    protected void doCode() throws Exception {
        JSONObject re = new JSONObject();
        Properties properties = new Properties();
        properties.load(new StringReader(source));
        properties.entrySet().stream()
            .forEach(objectObjectEntry -> re.put(objectObjectEntry.getKey().toString(), objectObjectEntry.getValue()));
        handle(new JsonResult(re));
    }
}