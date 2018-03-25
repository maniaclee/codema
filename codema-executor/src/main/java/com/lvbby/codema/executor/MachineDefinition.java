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

    private static final long serialVersionUID = -3219987384733432231L;
    /**
     * 唯一表示，name
     */
    private String id;
    /** 描述 */
    private String description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<MachineProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<MachineProperty> properties) {
        this.properties = properties;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getOutType() {
        return outType;
    }

    public void setOutType(String outType) {
        this.outType = outType;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}