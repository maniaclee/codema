package com.lvbby.codema.java.entity;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.google.common.collect.Maps;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.source.JavaClassSourceParser;
import com.lvbby.codema.java.tool.JavaClassUtils;
import com.lvbby.codema.java.tool.JavaLexer;
import com.lvbby.codema.java.tool.JavaSrcLoader;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 17/1/5.
 */
public class JavaClass extends AnnotationType{
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
        CompilationUnit src = JavaSrcLoader.getJavaSrcCompilationUnit(clz);

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

    /***
     * 设置name和package
     * @param s
     * @return
     */
    public JavaClass name(String s){
        if(StringUtils.isNotBlank(s)) {
            setName(ReflectionUtils.getSimpleClassName(s));
            setPack(ReflectionUtils.getPackage(s));
        }
        return this;
    }

    /***
     * 根据名称查找方法，选任意一个，如果有重载的任选一个
     * @param method
     * @return
     */
    public JavaMethod findMethodByName(String method){
        List<JavaMethod> collect = getMethods().stream().filter(method1 -> method1.getName().equals(method))
                .collect(Collectors.toList());
        if(collect.isEmpty()){
            return null;
        }
        Validate.isTrue(collect.size()==1,"multi method found : %s" , method);
        return collect.get(0);
    }


    public String  parseSymbolAsFullClassName(String classSymbol){
        if(StringUtils.isBlank(classSymbol))
            return null;
        //全路径名，直接返回
        if (classSymbol.contains("."))
            return classSymbol;
        ClassOrInterfaceDeclaration classOrInterfaceDeclaration = JavaLexer.getClass(src).get();
        //类型就是自己
        if (StringUtils.equals(classSymbol, classOrInterfaceDeclaration.getNameAsString())) {
            return classFullName();
        }
        return src.getImports().stream()
                .map(importDeclaration -> importDeclaration.toString().trim().replaceAll(";", "")
                        .split("\\s+")[1])
                .filter(importDeclaration -> importDeclaration.endsWith(classSymbol)).findAny().orElse(null);
    }

    public JavaClass parseSymbolAsClass(String classSymbol){
        String s = parseSymbolAsFullClassName(classSymbol);
        if(s==null)
            return null;
        if(StringUtils.equals(s,classFullName())){
            return this;
        }
        try {
            return JavaClassSourceParser.fromClassFullName(s).loadSource().get(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String javaSrc(){
        return src.toString();
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
