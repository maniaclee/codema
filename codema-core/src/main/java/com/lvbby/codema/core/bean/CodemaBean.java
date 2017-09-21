package com.lvbby.codema.core.bean;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by lipeng on 2016/12/31.
 */
public class CodemaBean {
    private String id;
    private Object resource;
    private List<Class> types;
    private CommonCodemaConfig config;

    public CodemaBean(Object obj) {
        setResource(obj);
    }

    public CodemaBean bindConfig(CommonCodemaConfig config){
        setConfig(config);
        return this;
    }

    public void initType(boolean includeSuperClass, boolean includeInterface) {
        setTypes(extractTypes(getResource().getClass(), includeSuperClass, includeInterface));
    }

    private static List<Class> extractTypes(Class clz, boolean includeSuperClass, boolean includeInterface) {
        ArrayList<Class> re = Lists.newArrayList(clz);
        if (includeSuperClass)
            for (Class parent = clz; parent != null; re.add(clz), clz = clz.getSuperclass()) ;
        if (includeInterface)
            re.addAll(Arrays.asList(clz.getInterfaces()));
        return re;
    }

    public boolean isValid() {
        return StringUtils.isNotBlank(id) && resource != null && CollectionUtils.isNotEmpty(types);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getResource() {
        return resource;
    }

    public void setResource(Object resource) {
        this.resource = resource;
    }

    public List<Class> getTypes() {
        return types;
    }

    public void setTypes(List<Class> types) {
        this.types = types;
    }

    public boolean match(Class clz) {
        return types != null && types.stream().anyMatch(aClass -> aClass.isAssignableFrom(clz));
    }

    public boolean match(String id) {
        return Objects.equals(id, getId());
    }

    public boolean match(String id, Class cLass) {
        return match(id) && match(cLass);
    }

    /**
     * Getter method for property   config.
     *
     * @return property value of config
     */
    public CommonCodemaConfig getConfig() {
        return config;
    }

    /**
     * Setter method for property   config .
     *
     * @param config  value to be assigned to property config
     */
    public void setConfig(CommonCodemaConfig config) {
        this.config = config;
    }
}
