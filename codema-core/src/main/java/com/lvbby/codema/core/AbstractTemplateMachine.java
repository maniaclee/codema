package com.lvbby.codema.core;

/**
 * 最基础的抽象类，实现基本功能和一些模板方法
 * @author dushang.lp
 * @version $Id: AbstractCodemaMachine.java, v 0.1 2017-08-24 3:32 dushang.lp Exp $
 */
public abstract class AbstractTemplateMachine<S, O> extends AbstractBaseMachine<S, O> implements TemplateCapable {
    private String template;

    @Override
    public String getTemplate() {
        return template;
    }

    @Override
    public void setTemplate(String template) {
        this.template = template;
    }
}