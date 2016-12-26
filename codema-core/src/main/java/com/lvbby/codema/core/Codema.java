package com.lvbby.codema.core;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.ServiceLoader;

/**
 * Created by lipeng on 16/12/23.
 */
public class Codema {
    private ConfigLoader configLoader;
    private List<CodemaMachine> codemaMachines;

    public static Codema fromYaml(String yaml) throws Exception {
        ConfigLoader configLoader = new YamlConfigLoader();
        configLoader.load(yaml);
        return new Codema(configLoader);
    }

    public Codema(ConfigLoader configLoader) {
        this.configLoader = configLoader;
        this.codemaMachines = loadCodemaMachines();
    }


    public <T extends CodemaMachine> T getCodemaMachineByType(Class<T> clz) {
        return (T) codemaMachines.stream().filter(codemaMachine -> codemaMachine.getClass().equals(clz));
    }

    public void run() throws Exception {
        /** 整个codema生命周期内共用一个context */
        CodemaContext codemaContext = new CodemaContext();
        codemaContext.setConfigLoader(configLoader);
        for (CodemaMachine codemaMachine : codemaMachines) {
            handleCodemaMachine(codemaContext, codemaMachine);
        }
    }

    private void handleCodemaMachine(CodemaContext codemaContext, CodemaMachine codemaMachine) throws Exception {
        codemaMachine.code(codemaContext);
        //        /** 查找resultHandler来处理结果，优先级 CodemaResult.resultHandler, CoderCommonConfig.findResultHandler*/
        //        if (result != null && result.getResult() != null) {
        //            ResultHandler resultHandler = codemaContext.loadConfig(CoderCommonConfig.class).map(e -> e.findResultHandler()).orElse(result.getResultHandler());
        //            if (resultHandler != null) {
        //                resultHandler.handle(codemaContext, result);
        //                codemaContext.storeParam(result.getResult());//存储结果给其他模块调用
        //            }
        //        }
    }

    public Codema addCodemaMachine(CodemaMachine codemaMachine) {
        this.codemaMachines.add(codemaMachine);
        return this;
    }

    /***
     * 加载CodeMachine，通过CodeMachine的注解ConfigBind来从Yaml进行筛选，减少不必要的执行，不筛选也行，
     */
    private List<CodemaMachine> loadCodemaMachines() {
        return Lists.newArrayList(ServiceLoader.load(CodemaMachine.class));
    }

}
