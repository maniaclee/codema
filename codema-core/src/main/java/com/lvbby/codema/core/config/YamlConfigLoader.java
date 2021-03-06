package com.lvbby.codema.core.config;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.yaml.snakeyaml.Yaml;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 16/12/22.
 */
public class YamlConfigLoader implements ConfigLoader {

    private Map<String, Object> cache = Maps.newConcurrentMap();
    Map<String, Object> yamlMap;

    private CaseFormat caseFormat = CaseFormat.LOWER_HYPHEN;

    @Override
    public void load(String code) throws Exception {
        Yaml yaml = new Yaml();
        Iterable<Object> result = yaml.loadAll(code);
        if (result == null || !result.iterator().hasNext())
            throw new IllegalArgumentException("no configuration found");
        yamlMap = (Map<String, Object>) result.iterator().next();
        formatMap2cameCase(yamlMap);
    }

    @Override
    public <T> T getConfig(Class<T> clz) {
        if (cache.containsKey(clz.getName()))
            return (T) cache.get(clz.getName());
        String configKey = findConfigKey(clz);
        Map<String, Object> map = findMap(configKey);
        if (map == null)
            return null;
        DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
        T re = dozerBeanMapper.map(map, clz);
        /** 递归把父类的信息一并加载，同时顺便将父类懒惰初始化 */
        for (Class<?> parent = clz.getSuperclass(); parent != null && parent != Object.class; parent = parent.getSuperclass()) {
            copy(re, getConfig(parent));
        }
        cache.put(clz.getName(), re);
        return re;
    }

    private void copy(Object dest, Object other) {
        if (dest == null || other == null)
            return;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(dest.getClass(), Object.class);
            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                String propertyName = propertyDescriptor.getName();
                Object value = propertyDescriptor.getReadMethod().invoke(dest);
                Object otherValue = getPropertyValue(other, propertyName);
                if (value == null && otherValue != null) {
                    propertyDescriptor.getWriteMethod().invoke(dest, otherValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object getPropertyValue(Object obj, String propertyName) {
        try {
            for (Field field : obj.getClass().getDeclaredFields()) {
                if (field.getName().equals(propertyName)) {
                    field.setAccessible(true);
                    return field.get(obj);

                }
            }
            /** apache's BeanUtils.getProperty 会把List<String> 转为String，妹的 */
            //            return BeanUtils.getProperty(obj, propertyName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String findConfigKey(Class<?> clz) {
        ConfigKey annotation = clz.getAnnotation(ConfigKey.class);
        /** guess using class name , convert class name into a.b.camelCase*/
        if (annotation == null || StringUtils.isBlank(annotation.value())) {
            String clzSimpleName = clz.getSimpleName();
            String suffix = "CodemaConfig";
            if (clzSimpleName.endsWith(suffix))
                return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, clzSimpleName.substring(0, clzSimpleName.length() - suffix.length())).replaceAll("_", ".");
            throw new IllegalArgumentException("config not found for:" + clz.getName());
        }
        return annotation.value();
    }

    private Map<String, Object> findMap(String s) {
        if (StringUtils.isBlank(s))
            return null;
        s = s.trim();
        Map<String, Object> tmp = yamlMap;
        for (String e : s.split("\\.")) {
            if (!tmp.containsKey(e))
                return null;
            tmp = (Map<String, Object>) tmp.get(e);
        }
        return tmp;
    }

    /***
     * format all key to  camelCase
     */
    private void formatMap2cameCase(Map map) {
        Lists.newArrayList(map.keySet()).stream().filter(k -> k instanceof String).forEach(k -> {
            Object v = map.get(k);
            map.remove(k);
            if (v instanceof Map)
                formatMap2cameCase((Map) v);
            map.put(yamlCase2camelCase((String) k), v);
        });
    }

    /***
     * a.b.camelCase ->  a.b.camel-case
     * @param s
     * @return
     */
    private static List<String> toYamlPrefix(String s) {
        return Arrays.asList(s.split("\\.")).stream().map(e -> camelCase2yamlCase(e)).collect(Collectors.toList());
    }

    private static String camelCase2yamlCase(String s) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, s);
    }

    private static String yamlCase2camelCase(String s) {
        return CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, s);
    }

    public CaseFormat getCaseFormat() {
        return caseFormat;
    }

    public void setCaseFormat(CaseFormat caseFormat) {
        this.caseFormat = caseFormat;
    }

}
