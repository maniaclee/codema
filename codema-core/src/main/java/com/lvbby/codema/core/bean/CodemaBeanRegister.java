package com.lvbby.codema.core.bean;

import java.util.List;

/**
 * 可以向容器里注入bean
 * @author dushang.lp
 * @version $Id: CodemaFactoryBean.java, v 0.1 2017-11-09 上午12:03 dushang.lp Exp $$
 */
public interface CodemaBeanRegister {
    List<CodemaBean> getObjects();
}