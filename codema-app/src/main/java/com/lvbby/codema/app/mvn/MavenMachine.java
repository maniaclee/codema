package com.lvbby.codema.app.mvn;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.Machine;
import com.lvbby.codema.core.render.XmlTemplateResult;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.core.result.WriteMode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.List;

/**
 * Created by lipeng on 16/12/23.
 */
public class MavenMachine extends AbstractBaseMachine<MavenMachine,MavenMachine> {
    private String name;
    private String artifactId;
    private String groupId;
    private String version = "1.0";
    private String javaVersion = "1.8";
    /**
     * 目标src路径
     */
    private String destSrcRoot = "src/main/java";
    private String destResourceRoot = "src/main/resources";

    private String destTestSrcRoot = "src/test/java";
    private String destTestResourceRoot = "src/test/resources";

    private transient boolean mavenInitialized = false;
    private List<MavenMachine> modules;

    @Override
    public Machine<MavenMachine, MavenMachine> source(MavenMachine source) {
        //把自己加入到parent的modules里
        if(source!=null ){
            if(source.modules==null){
                source.modules=Lists.newLinkedList();
            }
            source.modules.add(this);
        }
        return super.source(source);
    }

    public void doCode() throws Exception {
        //创建目录，写入pom.xml,.gitignore
        initMaven();
        //设置result，传递给子maven
        setResult(new BasicResult<MavenMachine>().of(this));
        /** .git ignore */
        handleSimple(new BasicResult()
                .result(loadResourceAsString(".gitignore"))
                .filePath(getDestRootDir(),".gitignore")
        );
        handleSimple(new XmlTemplateResult(loadResourceAsString("pom.xml"))
                .bind("parent",getParent())
                .bind("config",this)
                .filePath(getDestRootDir(),"pom.xml")
                .writeMode(WriteMode.writeIfNoExist)
        );
    }
    public void initMaven() {
        if(mavenInitialized)
            return;
        Validate.notBlank(name, "name can't be blank");
        Validate.notBlank(artifactId, "[%s] artifactId can't be blank", _getId());
        Validate.notBlank(groupId, "[%s] groupId can't be blank", _getId());

        //设置maven项目的路径
        if (getParent() != null) {
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
    }
    public MavenMachine getParent() {
        return source;
    }
    /**
     * 标识符，打印日志用
     *
     * @return
     */
    private String _getId() {
        return String.format("%s[%s]", getClass().getSimpleName(), name);
    }

    /**
     * Getter method for property   name.
     *
     * @return property value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for property   name .
     *
     * @param name  value to be assigned to property name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for property   artifactId.
     *
     * @return property value of artifactId
     */
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * Setter method for property   artifactId .
     *
     * @param artifactId  value to be assigned to property artifactId
     */
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    /**
     * Getter method for property   groupId.
     *
     * @return property value of groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * Setter method for property   groupId .
     *
     * @param groupId  value to be assigned to property groupId
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * Getter method for property   version.
     *
     * @return property value of version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Setter method for property   version .
     *
     * @param version  value to be assigned to property version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Getter method for property   javaVersion.
     *
     * @return property value of javaVersion
     */
    public String getJavaVersion() {
        return javaVersion;
    }

    /**
     * Setter method for property   javaVersion .
     *
     * @param javaVersion  value to be assigned to property javaVersion
     */
    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
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

    /**
     * Getter method for property   destTestSrcRoot.
     *
     * @return property value of destTestSrcRoot
     */
    public String getDestTestSrcRoot() {
        return destTestSrcRoot;
    }

    /**
     * Setter method for property   destTestSrcRoot .
     *
     * @param destTestSrcRoot  value to be assigned to property destTestSrcRoot
     */
    public void setDestTestSrcRoot(String destTestSrcRoot) {
        this.destTestSrcRoot = destTestSrcRoot;
    }

    /**
     * Getter method for property   destTestResourceRoot.
     *
     * @return property value of destTestResourceRoot
     */
    public String getDestTestResourceRoot() {
        return destTestResourceRoot;
    }

    /**
     * Setter method for property   destTestResourceRoot .
     *
     * @param destTestResourceRoot  value to be assigned to property destTestResourceRoot
     */
    public void setDestTestResourceRoot(String destTestResourceRoot) {
        this.destTestResourceRoot = destTestResourceRoot;
    }

    /**
     * Getter method for property   modules.
     *
     * @return property value of modules
     */
    public List<MavenMachine> getModules() {
        return modules;
    }

}
