package com.lvbby.codema.core.machine.impl;

import com.alibaba.fastjson.JSONObject;
import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.result.BasicResult;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

/**
 * properties
 * @author dushang.lp
 * @version $Id: JsonDataSourceMachine.java, v 0.1 2018年01月24日 下午6:14 dushang.lp Exp $
 */
public class ToPropertiesMachine extends AbstractBaseMachine<JSONObject, String> {
    @Override
    protected void doCode() throws Exception {
        Properties properties = new Properties();
        source.entrySet().stream().filter(en -> en.getValue() != null).forEach(entry -> properties.put(entry.getKey(), source.getString(entry.getKey())));
        handle(BasicResult.instance(propertiesToString(properties)));
    }
    public  static String propertiesToString(Properties properties) throws IOException {
        StringWriter writer = new StringWriter();
        properties.store(writer,null);
        return writer.toString();
    }
}