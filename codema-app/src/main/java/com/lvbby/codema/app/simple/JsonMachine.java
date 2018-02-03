package com.lvbby.codema.app.simple;

import com.alibaba.fastjson.JSON;
import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.result.BasicResult;

/**
 *  格式化json
 * @author dushang.lp
 * @version $Id: JsonMachine.java, v 0.1 2017年12月15日 下午8:32 dushang.lp Exp $
 */
public class JsonMachine extends AbstractBaseMachine<String, String> {
    @Override protected void doCode() throws Exception {
        handle(BasicResult.instance(JSON.toJSONString(JSON.parseObject(source), true)));
    }
}