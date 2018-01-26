package com.lvbby.codema.core.machine.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lvbby.codema.core.AbstractBaseMachine;

/**
 *
 * @author dushang.lp
 * @version $Id: JsonDataSourceMachine.java, v 0.1 2018年01月24日 下午6:14 dushang.lp Exp $
 */
public class JsonDataSourceMachine extends AbstractBaseMachine<String, JSONObject> {
    @Override
    protected void doCode() throws Exception {
        handle(new JsonResult(JSON.parseObject(source)));
    }
}