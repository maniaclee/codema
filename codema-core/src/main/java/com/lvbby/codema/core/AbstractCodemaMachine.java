package com.lvbby.codema.core;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.utils.ReflectionUtils;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author dushang.lp
 * @version $Id: AbstractCodemaMachine.java, v 0.1 2017-08-24 3:32 dushang.lp Exp $
 */
public abstract class AbstractCodemaMachine<T extends CommonCodemaConfig>
                                           implements CodemaMachine<T> {

    protected String loadResourceAsString(String resourceName) throws IOException {
        return ReflectionUtils.loadResource(getClass(), resourceName);
    }

    protected List loadFrom(CodemaContext context, T config) {
        if (config.getFromConfig() != null) {
            List<Object> beans = context.getCodemaBeanFactory().getBeans(Object.class,
                codemaBean -> codemaBean.getConfig() != null && config.getFromConfig().getClass()
                    .isAssignableFrom(codemaBean.getConfig().getClass()));
            return beans;
        }
        if (config.isFromSource()) {
            return Lists.newArrayList(context.getSource());
        }
        return Lists.newLinkedList();
    }
}