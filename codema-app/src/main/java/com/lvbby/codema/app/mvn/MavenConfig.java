package com.lvbby.codema.app.mvn;

import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.config.ConfigKey;
import com.lvbby.codema.core.config.PostProcess;
import com.lvbby.codema.core.config.RecursiveConfigField;
import org.apache.commons.collections.CollectionUtils;

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
    private String version;
    private String javaVersion = "1.8";
    @RecursiveConfigField
    private List<MavenConfig> modules;

    private transient MavenConfig parent;

    public File findRootDir() {
        if (getParent() != null) {
            return new File(getParent().findRootDir(), name);
        }
        return new File(getDestRootDir(), name);
    }

    /***
     * 递归将children的parent设为自己
     */
    @PostProcess
    public void init() {
        if (CollectionUtils.isNotEmpty(getModules())) {
            getModules().forEach(child -> {
                child.setParent(this);
                child.init();
            });
        }
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
}
