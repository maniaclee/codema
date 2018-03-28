package com.lvbby.codema.core;

import com.google.common.collect.Maps;
import com.lvbby.codema.core.bean.CodemaBeanFactory;
import com.lvbby.codema.core.bean.DefaultCodemaBeanFactory;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.Map;

/**
 * Created by lipeng on 16/12/23.
 */
public class CodemaContext {
    private CodemaBeanFactory codemaBeanFactory = new DefaultCodemaBeanFactory();

    private Machine currentMachine;

    Map<Class, Object> paramMap = Maps.newConcurrentMap();

    public <T> T findBeanBlur(Class<T> clz, String id) {
        List<T> beans = codemaBeanFactory
                .getBeans(clz, codemaBean -> codemaBean.getId().contains(id));
        if (beans.isEmpty())
            return null;
        Validate.isTrue(beans.size() == 1, "multi bean found for %s", id);
        return beans.get(0);
    }

    public void storeParam(Object result) {
        paramMap.put(result.getClass(), result);
    }

    public CodemaBeanFactory getCodemaBeanFactory() {
        return codemaBeanFactory;
    }

    public void setCodemaBeanFactory(CodemaBeanFactory codemaBeanFactory) {
        this.codemaBeanFactory = codemaBeanFactory;
    }

    /**
     * Getter method for property   currentMachine.
     *
     * @return property value of currentMachine
     */
    public Machine getCurrentMachine() {
        return currentMachine;
    }

    /**
     * Setter method for property   currentMachine .
     *
     * @param currentMachine  value to be assigned to property currentMachine
     */
    public void setCurrentMachine(Machine currentMachine) {
        this.currentMachine = currentMachine;
    }
}
