package com.lvbby.codema.app.mybatis;

import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.resource.Resource;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;

import java.util.List;
import java.util.function.Function;

/**
 * Created by lipeng on 2017/11/08.
 */
@ConfigBind(MybatisCodemaMachine.class)
public class MybatisCodemaConfig extends JavaBasicCodemaConfig {
    private String mapperDir;
    private String configPackage;
    private boolean needConfigClass = false;
    private List<Resource> mapperXmlTemplates;
    /***
     * 表名到mapper xml的映射关系
     */
    private Function<String,String> table2mapperName = s -> s+".xml";

    public void setNeedConfigClass(boolean needConfigClass) {
        this.needConfigClass = needConfigClass;
    }

    public boolean isNeedConfigClass() {
        return this.needConfigClass;
    }

    public String getConfigPackage() {
        return configPackage;
    }

    public void setConfigPackage(String configPackage) {
        this.configPackage = configPackage;
    }

    public String getMapperDir() {
        return mapperDir;
    }

    public void setMapperDir(String mapperDir) {
        this.mapperDir = mapperDir;
    }

    public List<Resource> getMapperXmlTemplates() {
        return mapperXmlTemplates;
    }

    public void setMapperXmlTemplates(List<Resource> mapperXmlTemplates) {
        this.mapperXmlTemplates = mapperXmlTemplates;
    }

    public Function<String, String> getTable2mapperName() {
        return table2mapperName;
    }

    public void setTable2mapperName(Function<String, String> table2mapperName) {
        this.table2mapperName = table2mapperName;
    }
}
