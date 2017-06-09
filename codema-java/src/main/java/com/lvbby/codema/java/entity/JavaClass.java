package com.lvbby.codema.java.entity;

import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.core.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

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
    /** 源代码 */
    private transient CompilationUnit src;
    private Class type;

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
