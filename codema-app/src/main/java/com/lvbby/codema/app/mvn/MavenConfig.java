package com.lvbby.codema.app.mvn;

import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.config.PostProcess;
import com.lvbby.codema.core.config.RecursiveConfigField;
import com.lvbby.codema.core.utils.FileUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;

/**
 * Created by lipeng on 2017/1/3.
 */
@ConfigBind(MavenCodemaMachine.class)
public class MavenConfig extends CommonCodemaConfig {

    private String name;
    private String artifactId;
    private String groupId;
    private String version = "1.0";
    private String javaVersion = "1.8";
    @RecursiveConfigField
    private List<MavenConfig> modules;
    /**
     * 目标src路径
     */
    private String destSrcRoot;
    private String destResourceRoot;

    private transient MavenConfig parent;

    /***
     * 递归将children的parent设为自己
     */
    @PostProcess
    public void init() {
        //设置maven项目的路径
        File dir = parseRoot();
        if(dir!=null){
            setDestRootDir(dir.getAbsolutePath());
        }

        setDestSrcRoot(FileUtils.parseFileWithParentAsString(getDestRootDir(),getDestSrcRoot()));
        setDestResourceRoot(FileUtils.parseFileWithParentAsString(getDestRootDir(),getDestResourceRoot()));
        if (CollectionUtils.isNotEmpty(getModules())) {
            getModules().forEach(child -> {
                child.setParent(this);
                child.init();
            });
        }
    }
    private File parseRoot(){
        if(parent==null){
            if(StringUtils.isNotBlank(getDestRootDir())){
                return FileUtils.parseFile(getDestRootDir(),getName());
            }
            return null;
        }
        if (FileUtils.isRelativeFilePath(getDestRootDir())){
            return FileUtils.parseFile(getDestRootDir(),getName());
        }
        if(StringUtils.isBlank(getDestRootDir())){
            return FileUtils.parseFile(parent.getDestRootDir(),getName());
        }
        return FileUtils.parseFile(parent.getDestRootDir(),getDestRootDir(),getName());
    }

    public MavenConfig getParent() {
        return parent;
    }

    public void setParent(MavenConfig parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public List<MavenConfig> getModules() {
        return modules;
    }

    public void setModules(List<MavenConfig> modules) {
        this.modules = modules;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDestSrcRoot() {
        return destSrcRoot;
    }

    public void setDestSrcRoot(String destSrcRoot) {
        this.destSrcRoot = destSrcRoot;
    }

    public String getDestResourceRoot() {
        return destResourceRoot;
    }

    public void setDestResourceRoot(String destResourceRoot) {
        this.destResourceRoot = destResourceRoot;
    }
}
