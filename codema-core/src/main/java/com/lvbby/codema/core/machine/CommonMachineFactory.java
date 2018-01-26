package com.lvbby.codema.core.machine;

import com.alibaba.fastjson.JSONObject;
import com.lvbby.codema.core.Machine;
import com.lvbby.codema.core.machine.impl.JsonDataSourceMachine;
import com.lvbby.codema.core.machine.impl.PropertiesSourceMachine;
import com.lvbby.codema.core.machine.impl.SqlDataSourceMachine;
import com.lvbby.codema.core.machine.impl.ToPropertiesMachine;
import com.lvbby.codema.core.machine.impl.ToYamlMachine;

/**
 * 常用的source machine集合工厂
 * @author dushang.lp
 * @version $Id: DataSrcMachineFactory.java, v 0.1 2018年01月24日 下午8:37 dushang.lp Exp $
 */
public class CommonMachineFactory {

    public static Machine<String, JSONObject> fromJson() {
        return new JsonDataSourceMachine();
    }

    public static Machine<String, JSONObject> fromSqlInsert() {
        return new SqlDataSourceMachine();
    }

    public static Machine<String, JSONObject> fromProperties() {
        return new PropertiesSourceMachine();
    }

    public static Machine<JSONObject, String> toYaml() {
        return new ToYamlMachine();
    }
    public static Machine<JSONObject, String> toProperties() {
        return new ToPropertiesMachine();
    }
}