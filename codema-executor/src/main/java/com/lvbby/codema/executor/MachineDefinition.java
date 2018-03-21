package com.lvbby.codema.executor;

import com.lvbby.codema.core.bean.BaseEntity;

import java.util.List;

/**
 * 单个machine的定义
 *
 * @author dushang.lp
 * @version $Id: MachineMeta.java, v 0.1 2018年03月21日 下午7:15 dushang.lp Exp $
 */
public class MachineDefinition extends BaseEntity {

    /**
     * 唯一表示，name
     */
    private String id;
    /** 描述 */
    private String desc;
    /**
     * 给用户输入的自定义参数
     */
    private List<MachineProperty> properties;
    /**
     * 输入类型
     */
    private String sourceType;
    /**
     * 输入类型
     */
    private String outType;

    /**
     * template
     */
    private String template;
}