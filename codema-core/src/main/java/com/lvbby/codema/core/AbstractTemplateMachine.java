package com.lvbby.codema.core;

import lombok.Getter;
import lombok.Setter;

/**
 * 最基础的抽象类，实现基本功能和一些模板方法
 * @author dushang.lp
 * @version $Id: AbstractCodemaMachine.java, v 0.1 2017-08-24 3:32 dushang.lp Exp $
 */
public abstract class AbstractTemplateMachine<S, O> extends AbstractBaseMachine<S, O> implements TemplateCapable {
    @Getter
    @Setter
    private String template;

}