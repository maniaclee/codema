package com.lvbby.codema.java.entity;

import com.github.javaparser.ast.CompilationUnit;
import com.google.common.collect.Maps;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.tool.JavaCodeUtils;
import com.lvbby.codema.java.tool.JavaLexer;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 17/1/5.
 */
public class JavaClass {
    private String pack;
    private String name;
    private List<JavaField> fields;
    private List<JavaMethod> methods;
    private List<String> imports;
    /**
     * 来源于什么对象
     */
    private Object from;
    /**
     * 源代码
     */
    private transient CompilationUnit src;
    private Class type;

    /**
     * 缓存
     */
    private static Map<Class, JavaClass> cache = Maps.newHashMap();

    public static JavaClass from(Class clz) throws Exception {
        if (cache.containsKey(clz))
            return cache.get(clz);
        CompilationUnit src = JavaCodeUtils.loadJavaSrcFromProject(clz);

        JavaClass re = new JavaClass();
        re.setName(clz.getSimpleName());
        re.setFrom(clz);
        re.setPack(clz.getPackage().getName());
        re.setType(clz);

        re.setFields(JavaField.from(clz));
        re.setMethods(JavaMethod.from(clz).stream().map(method -> method.src(JavaLexer.getClass(src).orElse(null))).collect(Collectors.toList()));
        re.setSrc(src);
        cache.put(clz, re);
        return re;
    }

    public JavaClass() {
    }

    public String classFullName() {
        return StringUtils.isBlank(pack) ? name : (pack + "." + name);
    }

    public String getNameCamel() {
        return ReflectionUtils.camel(name);
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JavaField> getFields() {
        return fields;
    }

    public void setFields(List<JavaField> fields) {
        this.fields = fields;
    }

    public List<JavaMethod> getMethods() {
        return methods;
    }

    public void setMethods(List<JavaMethod> methods) {
        this.methods = methods;
    }

    public Object getFrom() {
        return from;
    }

    public void setFrom(Object from) {
        this.from = from;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public CompilationUnit getSrc() {
        return src;
    }

    public void setSrc(CompilationUnit src) {
        this.src = src;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JavaClass javaClass = (JavaClass) o;

        if (!pack.equals(javaClass.pack)) return false;
        return name.equals(javaClass.name);
    }

    @Override
    public int hashCode() {
        int result = pack.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
