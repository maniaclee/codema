package com.lvbby.codema.core.tool.mysql.machine;

import com.alibaba.fastjson.JSONObject;
import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.machine.impl.JsonResult;
import com.lvbby.codema.core.tool.mysql.SqlParser;

/**
 * Created by lipeng on 2018/4/11.
 */
public class SqlInsertToJsonSourceMachine extends AbstractBaseMachine<String, JSONObject> {
    @Override
    protected void doCode() throws Exception {
        //处理多个result
        handleList(SqlParser.parseInsert(source),jsonObject -> new JsonResult(jsonObject));
    }
}