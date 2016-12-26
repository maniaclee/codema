package com.lvbby.codema.java.baisc;

import com.github.javaparser.ast.CompilationUnit;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by lipeng on 2016/12/24.
 */
public class JavaSourceParam {
    private List<CompilationUnit> compilationUnits = Lists.newArrayList();

    public JavaSourceParam add(CompilationUnit compilationUnit) {
        compilationUnits.add(compilationUnit);
        return this;
    }

    public List<CompilationUnit> getCompilationUnits() {
        return compilationUnits;
    }

    public void setCompilationUnits(List<CompilationUnit> compilationUnits) {
        this.compilationUnits = compilationUnits;
    }
}
