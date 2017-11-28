package com.lvbby.codema.java.baisc;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.CodemaContextHolder;
import com.lvbby.codema.core.CodemaMachine;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.core.utils.FileUtils;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaCodemaMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;
import javassist.compiler.Javac;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

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
    public String relativeFile(String subFile){
        Validate.notBlank(getDestRootDir(),"destRootDir can't blank when using relativeFile : %s",subFile);
        if(StringUtils.isBlank(subFile)){
            return getDestRootDir();
        }
        return FileUtils.parseFile(getDestRootDir(), subFile).getAbsolutePath();
    }

    @Override public CodemaMachine loadCodemaMachine() {
        CodemaMachine codemaMachine = super.loadCodemaMachine();
        if(codemaMachine==null){
            JavaTemplateResource annotation = getClass().getAnnotation(JavaTemplateResource.class);
            if(annotation!=null && annotation.value()!=null){
                return new AbstractJavaCodemaMachine<JavaBasicCodemaConfig>() {
                    @Override protected Result<JavaClass> codeEach(JavaBasicCodemaConfig config, JavaClass source)
                            throws Exception {
                        return new JavaTemplateResult(config, annotation.value(), source);

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
        return javaClassNameParser.getClassName(
                (JavaClass) CodemaContextHolder.getCodemaContext().getSource(),javaClass);
    }
    public String parseDestClassFullName(JavaClass javaClass){
        return String.format("%s.%s", getDestPackage(),parseDestClassName(javaClass));
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
}
