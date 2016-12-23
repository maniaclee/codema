package com.lvbby.codema.core;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

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


    public void run() throws Exception {
        CodemaContext codemaContext = new CodemaContext();
        codemaContext.setConfigLoader(configLoader);
        for (CodemaMachine codemaMachine : codemaMachines) {
            codemaMachine.code(codemaContext);
        }
    }

    private List<CodemaMachine> loadCodemaMachines() {
        return Lists.newArrayList(ServiceLoader.load(CodemaMachine.class)).stream().filter(m -> useCodema(m.getClass())).collect(Collectors.toList());
    }

    private boolean useCodema(Class<?> codemaMachine) {
        return Optional.of(codemaMachine.getAnnotation(ConfigBind.class)).map(configBind -> configLoader.getConfig(configBind.value())).isPresent();
    }
}
