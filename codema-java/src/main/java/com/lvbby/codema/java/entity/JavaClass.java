package com.lvbby.codema.java.entity;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.tool.JavaClassUtils;
import com.lvbby.codema.java.tool.JavaLexer;
import com.lvbby.codema.java.tool.JavaSrcLoader;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
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
     * 源代码
     */
    private transient CompilationUnit src;
    private Class type;
    private boolean beInterface = false;

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
    public String getVarName(){
        return StringUtils.uncapitalize(getName());
    }

    public JavaClass removeMethod(Predicate<String> predicate){
        if(CollectionUtils.isNotEmpty(getMethods()) && predicate!=null) {
            getMethods().removeAll(getMethods().stream().filter(method -> predicate.test(method.getName())).collect(Collectors.toList()));
            CompilationUnit src = getSrc();
            if (src != null) {
                JavaLexer.getClass(src).ifPresent(
                        clz -> clz.getMethods().stream().filter(methodDeclaration -> predicate.test(methodDeclaration.getNameAsString()))
                                .forEach(methodDeclaration -> clz.remove(methodDeclaration)));
            }
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
        //从imports中找
        String result = src.getImports().stream()
                .map(importDeclaration -> importDeclaration.toString().trim().replaceAll(";", "")
                        .split("\\s+")[1])
                .filter(importDeclaration -> importDeclaration.endsWith(classSymbol)).findAny()
                .orElse(null);
        if(result==null){
            //从同级包内查找
            CompilationUnit javaSrcCompilationUnit = JavaSrcLoader
                    .getJavaSrcCompilationUnit(String.format("%s.%s", getPack(), classSymbol));
            if(javaSrcCompilationUnit!=null){
                Optional<ClassOrInterfaceDeclaration> clz = JavaLexer.getClass(javaSrcCompilationUnit);
                if(!clz.isPresent()){
                    return null;
                }
                String clzName = clz.get().getNameAsString();
                return javaSrcCompilationUnit.getPackageDeclaration().map(packageDeclaration -> String
                        .format("%s.%s", packageDeclaration.getNameAsString(),clzName)).orElse(null);
            }
        }
        return result;
    }

    public JavaClass parseSymbolAsClass(String classSymbol){
        String s = parseSymbolAsFullClassName(classSymbol);
        if(s==null)
            return null;
        if(StringUtils.equals(s,classFullName())){
            return this;
        }
        try {
            return JavaClassUtils.fromClassFullName(s);
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

    public boolean isBeInterface() {
        return beInterface;
    }

    public void setBeInterface(boolean beInterface) {
        this.beInterface = beInterface;
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
