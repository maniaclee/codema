package com.lvbby.codema.core.machine.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lvbby.codema.core.result.BasicResult;

/**
 * json result, format
 * @author dushang.lp
 * @version $Id: JsonResult.java, v 0.1 2018年01月25日 上午10:26 dushang.lp Exp $
 */
public class JsonResult extends BasicResult<JSONObject> {
    public JsonResult(JSONObject object) {
        result(object);
    }

    @Override
    public String getString() {
        return JSON.toJSONString(getResult(), true);
    }
}