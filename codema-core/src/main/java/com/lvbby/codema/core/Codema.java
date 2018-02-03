package com.lvbby.codema.core;

import com.lvbby.codema.core.handler.ResultHandlerFactory;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created by lipeng on 16/12/23.
 */
public class Codema {

    public static Machine link(Machine... machines) {
        if (machines != null && machines.length > 0) {
            Machine first = machines[0];
            for (int i = 1; i < machines.length; i++) {
                machines[i - 1].next(machines[i]);
            }
            return first;
        }
        return null;
    }

    public static void exec(List<ResultHandler> resultHandlers, Machine... machines) throws Exception {
        link(machines).resultHandlers(resultHandlers).run();
    }

    public static void exec(Machine... machines) throws Exception {
        link(machines).addResultHandler(ResultHandlerFactory.print).run();
    }

    public static <Input> void exec(List<Input> source, Supplier<Machine> machineFunction) throws Exception {
        Validate.notEmpty(source, "source list can't be empty");
        for (Input input : source) {
            exec(machineFunction.get().source(input));
        }
    }

}
