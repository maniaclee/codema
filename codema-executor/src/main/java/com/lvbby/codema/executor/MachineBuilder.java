package com.lvbby.codema.executor;

import com.lvbby.codema.core.BasicTemplateMachine;
import com.lvbby.codema.core.Machine;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2018/3/22.
 */
public class MachineBuilder {

    public static Machine build(String template, Map<String, Object> args) {
        BasicTemplateMachine<Object, Object> m = new BasicTemplateMachine<>();
        m.setTemplate(template);
        m.setArgs(args);
        return m;
    }


    /***
     *
     * @param machineDefinition
     * @param args
     * @return
     */
    public static Machine build(MachineDefinition machineDefinition, List<MachineRequestArg> args) {
        Map<String, Object> argMap = CollectionUtils.isEmpty(args) ? null : args.stream().collect(Collectors.toMap(o -> o.getKey(), o -> o.getValue()));
        return build(machineDefinition.getTemplate(), argMap);
    }
}
