package com.lvbby.codema.java.app.mvn;

import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.config.ConfigKey;

import java.util.List;

/**
 * Created by lipeng on 2017/1/3.
 */
@ConfigKey("maven")
public class MavenConfig extends CommonCodemaConfig {

    private String name;
    private String artifactId;
    private String groupId;
    private String javaVersion;
    private List<MavenConfig> modules;

    private transient MavenConfig parent;

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
}
