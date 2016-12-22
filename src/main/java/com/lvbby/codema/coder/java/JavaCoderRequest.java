package com.lvbby.codema.coder.java;

import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.coder.CoderBaseRequest;

/**
 * Created by lipeng on 2016/12/20.
 */
public class JavaCoderRequest extends CoderBaseRequest {

    private String pack;

    private boolean isInterface;
    private String className;


    /**
     * =================== results =============
     */
    private CompilationUnit result = new CompilationUnit();

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isInterface() {
        return isInterface;
    }


    public CompilationUnit getResult() {
        return result;
    }

    public void setResult(CompilationUnit result) {
        this.result = result;
    }

    public void setInterface(boolean anInterface) {
        isInterface = anInterface;
    }


    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }
}
