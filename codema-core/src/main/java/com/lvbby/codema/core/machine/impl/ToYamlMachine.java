package com.lvbby.codema.core.machine.impl;

import com.alibaba.fastjson.JSONObject;
import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.result.BasicResult;
import org.yaml.snakeyaml.Yaml;

/**
 * 生成yaml
 * @author dushang.lp
 * @version $Id: ToJsonMachine.java, v 0.1 2018年01月24日 下午8:28 dushang.lp Exp $
 */
public class ToYamlMachine extends AbstractBaseMachine<JSONObject, String> {

    @Override
    protected void doCode() throws Exception {
        handle(BasicResult.instance(new Yaml().dumpAsMap(source)));
    }
}