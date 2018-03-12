package com.lvbby.codema.app.mvn;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.config.NotBlank;
import com.lvbby.codema.core.utils.FileUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.util.List;

/**
 * Created by lipeng on 16/12/23.
 */
@Data
@NoArgsConstructor
public class MavenConfig  {
    @NotBlank
    private String name;
    @NotBlank
    private String artifactId;
    @NotBlank
    private String groupId;
    @NotBlank
    private String destRootDir;

    private String version = "1.0";
    private String javaVersion = "1.8";
    /**
     * 目标src路径
     */
    private String destSrcRoot = "src/main/java";
    private String destResourceRoot = "src/main/resources";

    private String destTestSrcRoot = "src/test/java";
    private String destTestResourceRoot = "src/test/resources";

    private List<MavenConfig> modules;
    private MavenConfig parent;

    private transient boolean mavenInitialized = false;

    public static MavenConfig instance(String destRootDir,String name,String artifactId,String groupId){
        MavenConfig re = new MavenConfig();
        re.setDestRootDir(destRootDir);
        re.setName(name);
        re.setArtifactId(artifactId);
        re.setGroupId(groupId);
        return re;
    }
    public void init() {
        if(mavenInitialized)
            return;
        //设置maven项目的路径
        if (getParent() != null) {
            if (StringUtils.isBlank(getDestRootDir())) {
                //将根路径设为parent/name
                setDestRootDir(new File(getParent().getDestRootDir(), name).getAbsolutePath());
            }
        }
        Validate.notBlank(destRootDir);
        setDestSrcRoot(parseFileWithParent(getDestRootDir(), getDestSrcRoot()));
        setDestResourceRoot(parseFileWithParent(getDestRootDir(), getDestResourceRoot()));
        setDestTestSrcRoot(parseFileWithParent(getDestRootDir(), getDestTestSrcRoot()));
        setDestTestResourceRoot(parseFileWithParent(getDestRootDir(), getDestTestResourceRoot()));
        mavenInitialized =true;
    }

    public MavenConfig addChild(MavenConfig mavenConfig){
        if(modules==null)
            modules = Lists.newLinkedList();
        modules.add(mavenConfig);
        mavenConfig.parent=this;
        return this;
    }

    private String parseFileWithParent(String destRootDir, String sub) {
        if (!FileUtils.isRelativeFilePath(sub)) {
            return sub;
        }
        return new File(destRootDir,sub).getAbsolutePath();
    }
}
