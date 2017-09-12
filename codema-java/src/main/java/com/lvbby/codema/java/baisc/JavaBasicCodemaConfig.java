package com.lvbby.codema.java.baisc;

import com.google.common.collect.*;
import com.lvbby.codema.core.*;
import com.lvbby.codema.core.config.*;
import com.lvbby.codema.java.entity.*;
import com.lvbby.codema.java.machine.*;
import com.lvbby.codema.java.result.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import org.apache.commons.lang3.*;

/**
 * Created by lipeng on 2016/12/22.
 */
public class JavaBasicCodemaConfig extends CommonCodemaConfig implements Serializable {
    /**
     * 目标package
     */
    private String destPackage;
    /**
     * 目标src路径
     */
    private String destSrcRoot;
    private String destResourceRoot;

    private String destClassName;
    /***
     * 获取目标bean的名称
     */
    private JavaClassNameParser javaClassNameParser = JavaClassNameParserFactory.defaultInstance();
    /**
     * 选取类的package
     */
    private String fromPackage;
    private boolean toBeInterface = false;
    private List<JavaClassNameParser>  implementInterfaces = Lists.newLinkedList();
    private String parentClass;

    public JavaBasicCodemaConfig addSubDestPackage(String pack) {
        setDestPackage(relativePackage(pack));
        return this;
    }

    /***
     * 基于destPackage计算最终的package
     * @param subPackage
     * @return
     */
    public String relativePackage(String subPackage) {
        if (StringUtils.isNotBlank(subPackage)) {
            if (StringUtils.isNotBlank(destPackage)) {
                subPackage = String.format("%s.%s", destPackage, subPackage);
            }
        }
        return subPackage;
    }

    @Override public CodemaMachine loadCodemaMachine() {
        CodemaMachine codemaMachine = super.loadCodemaMachine();
        if(codemaMachine==null){
            JavaTemplateResource annotation = getClass().getAnnotation(JavaTemplateResource.class);
            if(annotation!=null && annotation.value()!=null){
                return new AbstractJavaCodemaMachine() {
                    @Override protected void codeEach(CodemaContext context,
                                                      JavaBasicCodemaConfig config,
                                                      JavaClass javaClass) throws Exception {
                        config.handle(context, new JavaTemplateResult(config, annotation.value(), javaClass));
                    }

                };
            }
        }
        return codemaMachine;
    }

    /**
     * 解析目标类的名称
     * @param javaClass
     * @return
     */
    public String parseDestClassName(JavaClass javaClass){
        if(StringUtils.isNotBlank(destClassName)){
            return destClassName;
        }
        return javaClassNameParser.getClassName(
                (JavaClass) CodemaContextHolder.getCodemaContext().getSource(),javaClass);
    }

    public JavaBasicCodemaConfig addImplementInterface(JavaClassNameParser javaClassNameParser){
        implementInterfaces.add(javaClassNameParser);
        return this;
    }

    public String findImplementInterfacesAsString(JavaClass from) {
        return implementInterfaces.stream().map(javaClassNameParser1 -> javaClassNameParser1.getClassName(
                (JavaClass) CodemaContextHolder.getCodemaContext().getSource(), from)).collect(Collectors.joining(","));
    }

    public boolean isToBeInterface() {
        return toBeInterface;
    }

    public void setToBeInterface(boolean toBeInterface) {
        this.toBeInterface = toBeInterface;
    }

    public String getDestSrcRoot() {
        return destSrcRoot;
    }

    public void setDestSrcRoot(String srcRoot) {
        this.destSrcRoot = srcRoot;
    }

    public String getDestPackage() {
        return destPackage;
    }

    public void setDestPackage(String destPackage) {
        this.destPackage = destPackage;
    }

    public String getDestClassName() {
        return destClassName;
    }

    public void setDestClassName(String destClassName) {
        this.destClassName = destClassName;
    }

    public String getFromPackage() {
        return fromPackage;
    }

    public void setFromPackage(String fromPackage) {
        this.fromPackage = fromPackage;
    }

    public List<JavaClassNameParser> getImplementInterfaces() {
        return implementInterfaces;
    }

    public void setImplementInterfaces(List<JavaClassNameParser> implementInterfaces) {
        this.implementInterfaces = implementInterfaces;
    }

    public String getParentClass() {
        return parentClass;
    }

    public void setParentClass(String parentClass) {
        this.parentClass = parentClass;
    }

    public String getDestResourceRoot() {
        return destResourceRoot;
    }

    public void setDestResourceRoot(String destResourceRoot) {
        this.destResourceRoot = destResourceRoot;
    }

    public JavaClassNameParser getJavaClassNameParser() {
        return javaClassNameParser;
    }

    public void setJavaClassNameParser(JavaClassNameParser javaClassNameParser) {
        this.javaClassNameParser = javaClassNameParser;
    }


    /***
     * 将Java template class 转为codemaMachine
     * @param clz
     * @return
     */
    protected CodemaMachine load(Class clz) {
        return new AbstractJavaCodemaMachine() {
            @Override
            protected void codeEach(CodemaContext context, JavaBasicCodemaConfig config,
                                    JavaClass javaClass) throws Exception {

                config.handle(context, new JavaTemplateResult(config, clz, javaClass));
            }
        };
    }
}
