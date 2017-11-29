package com.lvbby.codema.java.machine;

import com.lvbby.codema.core.AbstractBaseCodemaMachine;
import com.lvbby.codema.core.CodemaContextHolder;
import com.lvbby.codema.core.config.ConfigProperty;
import com.lvbby.codema.java.baisc.JavaClassNameParser;
import com.lvbby.codema.java.baisc.JavaClassNameParserFactory;
import com.lvbby.codema.java.entity.JavaClass;

/**
 * 具有java的配置信息
 * Created by dushang.lp on 2017/8/16.
 */
public abstract class AbstractJavaCodemaMachine<S,O>
        extends AbstractBaseCodemaMachine<S, O> {

    /**
     * 目标package
     */
    @ConfigProperty
    private String destPackage;
    @ConfigProperty
    private String destSrcRoot;
    @ConfigProperty
    private String destResourceRoot;

    /***
     * 获取目标bean的名称
     */
    @ConfigProperty
    private JavaClassNameParser javaClassNameParser = JavaClassNameParserFactory.defaultInstance();

    public String parseDestClassName(JavaClass javaClass){
        return javaClassNameParser.getClassName(javaClass);
    }
    protected String parseDestClassFullName(JavaClass javaClass){
        return String.format("%s.%s", getDestPackage(),parseDestClassName(javaClass));
    }
    public String getDestPackage() {
        return destPackage;
    }

    public void setDestPackage(String destPackage) {
        this.destPackage = destPackage;
    }

    public JavaClassNameParser getJavaClassNameParser() {
        return javaClassNameParser;
    }

    public void setJavaClassNameParser(JavaClassNameParser javaClassNameParser) {
        this.javaClassNameParser = javaClassNameParser;
    }

    /**
     * Getter method for property   destSrcRoot.
     *
     * @return property value of destSrcRoot
     */
    public String getDestSrcRoot() {
        return destSrcRoot;
    }

    /**
     * Setter method for property   destSrcRoot .
     *
     * @param destSrcRoot  value to be assigned to property destSrcRoot
     */
    public void setDestSrcRoot(String destSrcRoot) {
        this.destSrcRoot = destSrcRoot;
    }

    /**
     * Getter method for property   destResourceRoot.
     *
     * @return property value of destResourceRoot
     */
    public String getDestResourceRoot() {
        return destResourceRoot;
    }

    /**
     * Setter method for property   destResourceRoot .
     *
     * @param destResourceRoot  value to be assigned to property destResourceRoot
     */
    public void setDestResourceRoot(String destResourceRoot) {
        this.destResourceRoot = destResourceRoot;
    }
}
