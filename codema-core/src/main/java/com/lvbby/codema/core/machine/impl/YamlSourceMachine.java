package com.lvbby.codema.core.machine.impl;

import com.alibaba.fastjson.JSONObject;
import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.Codema;
import org.yaml.snakeyaml.Yaml;

import java.io.StringReader;
import java.util.Map;

/**
 * yaml
 * @author dushang.lp
 * @version $Id: JsonDataSourceMachine.java, v 0.1 2018年01月24日 下午6:14 dushang.lp Exp $
 */
public class YamlSourceMachine extends AbstractBaseMachine<String, JSONObject> {
    @Override
    protected void doCode() throws Exception {
        Object load = new Yaml().load(new StringReader(source));
        handle(new JsonResult(new JSONObject((Map)load)));
    }

    public static void main(String[] args) throws Exception {
        String source = "rules:\n" + "    internal-no-invalid-meta: \"error\"\n" + "    internal-consistent-docs-description: \"error\"\n";
        Codema.execPrint(new YamlSourceMachine().source(source));
    }
}