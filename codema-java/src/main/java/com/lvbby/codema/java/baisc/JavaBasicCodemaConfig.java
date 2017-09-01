package com.lvbby.codema.java.baisc;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.CodemaMachine;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.config.ConfigKey;
import com.lvbby.codema.core.engine.ScriptEngineFactory;
import com.lvbby.codema.core.error.CodemaRuntimeException;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaCodemaMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lipeng on 2016/12/22.
 */
@ConfigKey("java.basic")
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
    private JavaClassNameParser javaClassNameParser = new DefaultJavaClassNameParser();
    /**
     * 选取类的package
     */
    private String fromPackage;
    private boolean toBeInterface = false;
    private List<String> implementInterfaces;
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

    public List<String> getImplementInterfaces() {
        return implementInterfaces;
    }

    public void setImplementInterfaces(List<String> implementInterfaces) {
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

    public String evalDestClassName(JavaClass javaClass, String defaultValue) {
        return eval(getDestClassName(), javaClass.getName(), defaultValue);
    }

    public String eval(String src, String param) {
        return eval(src, param, null);
    }

    public String eval(String src, String param, String defaultValue) {
        String s = ObjectUtils.firstNonNull(ScriptEngineFactory.instance.eval(src, param), defaultValue);
        if (s == null)
            throw new CodemaRuntimeException(String.format("eval %s return null !", src));
        return s;
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
