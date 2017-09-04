package com.lvbby.codema.app.mvn;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.config.CommonCodemaConfig;
import com.lvbby.codema.core.config.ConfigBind;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

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
    private List<MavenConfig> modules;
    /**
     * 目标src路径
     */
    private String destSrcRoot = "src/main/java";
    private String destResourceRoot = "src/main/resources";

    private String destTestSrcRoot = "src/test/java";
    private String destTestResourceRoot = "src/test/resources";

    private transient MavenConfig parent;

    private transient boolean mavenInitialized = false;

    public void init() {
        super.init();
        initMaven();
    }

    /***
     * 递归将children的parent设为自己
     */
    public void initMaven() {
        if(mavenInitialized)
            return;
        Validate.notBlank(name, "name can't be blank");
        Validate.notBlank(artifactId, "[%s] artifactId can't be blank", _getId());
        Validate.notBlank(groupId, "[%s] groupId can't be blank", _getId());

        //设置maven项目的路径
        if (parent != null) {
            if (StringUtils.isBlank(getDestRootDir())) {
                //将根路径设为parent
                setDestRootDir(getParent().getDestRootDir());
            } else {
                setDestRootDir(parseFileWithParent(getParent().getDestRootDir(), getDestRootDir(), String.format("%s.destRootDir", _getId(), name)));
            }
        }
        //将根路径设为maven的名称
        setDestRootDir(parseFileWithParent(getDestRootDir(), name, String.format("%s.destRootDir", _getId())));

        setDestSrcRoot(parseFileWithParent(getDestRootDir(), getDestSrcRoot(), String.format("%s.destSrcRoot", _getId(), name)));
        setDestResourceRoot(parseFileWithParent(getDestRootDir(), getDestResourceRoot(), String.format("%s.destResourceRoot", _getId(), name)));

        setDestTestSrcRoot(parseFileWithParent(getDestRootDir(), getDestTestSrcRoot(), String.format("%s.destTestSrcRoot", _getId(), name)));
        setDestTestResourceRoot(parseFileWithParent(getDestRootDir(), getDestTestResourceRoot(), String.format("%s.destTestResourceRoot", _getId(), name)));
        mavenInitialized =true;
        if (CollectionUtils.isNotEmpty(getModules())) {
            getModules().forEach(child -> {
                child.setParent(this);
                child.initMaven();
            });
        }
    }

    /**
     * 标识符，打印日志用
     *
     * @return
     */
    private String _getId() {
        return String.format("%s[%s]", getClass().getSimpleName(), name);
    }

    public static void main(String[] args) {
        MavenConfig parent = new MavenConfig();
        parent.setName("lee");
        parent.setDestRootDir("~");

        MavenConfig child = new MavenConfig();
        child.setName("child");
        parent.setModules(Lists.newArrayList(child));
        parent.init();
        System.out.println(parent.getDestRootDir());
        System.out.println(parent.getDestSrcRoot());
        System.out.println(child.getDestRootDir());
        System.out.println(child.getDestResourceRoot());
        System.out.println(child.getDestTestResourceRoot());

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

    public String getDestTestSrcRoot() {
        return destTestSrcRoot;
    }

    public void setDestTestSrcRoot(String destTestSrcRoot) {
        this.destTestSrcRoot = destTestSrcRoot;
    }

    public String getDestTestResourceRoot() {
        return destTestResourceRoot;
    }

    public void setDestTestResourceRoot(String destTestResourceRoot) {
        this.destTestResourceRoot = destTestResourceRoot;
    }
}
