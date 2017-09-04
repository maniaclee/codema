package com.lvbby.codema.java.tool;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lvbby.codema.java.entity.JavaClass;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author dushang.lp
 */
public class AutoImport extends VoidVisitorAdapter {
    /***
     * 全局可能的import候选集
     */
    private static Map<String, String> globalCandidates = Maps.newHashMap();

    private CompilationUnit            compilationUnit;
    private Set<String>                imports          = Sets.newHashSet();
    private Set<String>                excludeImports   = Sets.newHashSet();
    private Map<String, String>        candidates       = Maps.newHashMap();

    static {
        addGlobalCandidate(Date.class);
        addGlobalCandidate(List.class);
        addGlobalCandidate(Collection.class);
        addGlobalCandidate(Map.class);
        addGlobalCandidate(Set.class);
        addGlobalCandidate(Timestamp.class);
    }

    public static void addGlobalCandidate(Class clz) {
        globalCandidates.put(clz.getSimpleName(), clz.getName());
    }

    public AutoImport(CompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit;
    }

    public void addCandidate(String className , String classFullPath) {
        if (StringUtils.isNoneBlank(className, classFullPath)) {
            candidates.put(className, classFullPath);
        }
    }

    public void addCandidate(JavaClass javaClass) {
        if(javaClass!=null){
            addCandidate(javaClass.getName(),javaClass.classFullName());
        }
    }

    public CompilationUnit parse() {
        //把已经存在的放入已存在列表
        compilationUnit.getImports()
            .forEach(importDeclaration -> excludeImports.add(importDeclaration.getNameAsString()));
        compilationUnit.accept(this, null);
        imports.forEach(s -> compilationUnit.addImport(s));
        return getCompilationUnit();
    }

    @Override
    public void visit(ClassOrInterfaceType n, Object arg) {
        String typeName = n.toString();
        //泛型，如List<String> , Map<String,Map<Long,Object>>
        if(typeName.contains("<")){
            for (String s : typeName.split("[<>,]+")) {
                addImport(s);
            }
            return;
        }
        addImport(typeName);
    }

    @Override public void visit(NameExpr n, Object arg) {
        super.visit(n, arg);
        //静态方法中里静态类引用，如BuildUtils.build(s),BuildUtils需要解析
        if (StringUtils.isAllUpperCase(n.toString().substring(0,1))) {
            addImport(n.toString());
        }
    }


    private boolean addImport(String s) {
        return addImport(s, globalCandidates, candidates);
    }

    private boolean addImport(String s, Map<String, String>... candidates) {
        if(candidates==null || StringUtils.isBlank(s)){
            return false;
        }
        for (Map<String, String> cs : candidates) {
            if (excludeImports.contains(s)) {
                return false;
            }
            if (cs.containsKey(s)) {
                imports.add(cs.get(s));
                excludeImports.add(s);
                return true;
            }
        }
        return false;
    }


    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    public void setCompilationUnit(CompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit;
    }

    public static void main(String[] args) throws Exception {
        CompilationUnit src = JavaLexer.read(new File(
            "/Users/dushang.lp/workspace/test-codema/src/main/java/com/lvbby/test/codema/repo/SqlColumnDaoRepository.java"));
        AutoImport autoImport = new AutoImport(src);
        CompilationUnit parse = autoImport.parse();
        System.out.println("=======");
        //        parse.getImports().forEach(importDeclaration -> System.out.println(importDeclaration));
        autoImport.imports.forEach(importDeclaration -> System.out.println(importDeclaration));
    }
}