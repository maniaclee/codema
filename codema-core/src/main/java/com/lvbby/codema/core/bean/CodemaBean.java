package com.lvbby.codema.core.bean;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Created by lipeng on 2016/12/31.
 */
public class CodemaBean {
    private String id;
    private Object resource;
    private List<Class> types;
    //这里应该是一个纯粹的BeanFactory，不应该和codema绑定

    public CodemaBean(Object obj) {
        this(obj,null);
    }
    public <T> CodemaBean(T obj, Function<T,String> id) {
        setResource(obj);
        //默认做法
        initType(true,true);
        if(id!=null){
            setId(id.apply(obj));
        }else {
            setId(obj.getClass().getSimpleName());
        }
    }

    public void initType(boolean includeSuperClass, boolean includeInterface) {
        setTypes(extractTypes(getResource().getClass(), includeSuperClass, includeInterface));
    }

    private static List<Class> extractTypes(Class clz, boolean includeSuperClass, boolean includeInterface) {
        ArrayList<Class> re = Lists.newArrayList(clz);
        if (includeSuperClass)
            for (Class parent = clz; parent != null&&parent.equals(Object.class); re.add(parent), parent = parent.getSuperclass()) ;
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

}
