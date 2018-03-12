package com.lvbby.codema.app.mvn;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.config.NotBlank;
import com.lvbby.codema.core.utils.FileUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Created by lipeng on 16/12/23.
 */
@Data
@NoArgsConstructor
public class MavenConfig {
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
    private String destSrcRoot;
    private String destResourceRoot;

    private String destTestSrcRoot;
    private String destTestResourceRoot;

    private List<MavenConfig> modules;
    private MavenConfig parent;

    public static MavenConfig instance(String destRootDir, String artifactId, String groupId) {
        return new MavenConfig(destRootDir, artifactId, groupId);
    }

    public MavenConfig(String destRootDir, String artifactId, String groupId) {
        Validate.isTrue(!FileUtils.isRelativeFilePath(destRootDir));
        this.artifactId = artifactId;
        this.groupId = groupId;
        this.destRootDir = destRootDir;
        Function<String, String> f = s -> new File(destRootDir, s).getAbsolutePath();
        setDestSrcRoot(f.apply("src/main/java"));
        setDestResourceRoot(f.apply("src/main/resources"));
        setDestTestResourceRoot(f.apply("src/test/resources"));
        setDestTestSrcRoot(f.apply("src/test/java"));
    }

    public MavenConfig addChild(MavenConfig mavenConfig) {
        if (modules == null)
            modules = Lists.newLinkedList();
        modules.add(mavenConfig);
        mavenConfig.parent = this;
        return this;
    }

    public MavenConfig newChild(String name, String artifactId, String groupId) {
        MavenConfig child = new MavenConfig(new File(destRootDir, name).getAbsolutePath(), artifactId, groupId);
        addChild(child);
        return child;
    }

    public static List<MavenConfig> scan(List<File> roots, int maxDepth) {
        List<MavenConfig> result = Lists.newLinkedList();
        roots.forEach(file -> scanMaven(file, result, 1, maxDepth, null));
        return result;
    }

    public static boolean isMavenDirectory(File file) {
        return file != null && file.isDirectory() && file.exists() && new File(file, "pom.xml").exists();
    }
    private static void scanMaven(File file, List<MavenConfig> result, int depth, int maxDepth, MavenConfig parent) {
        if (maxDepth < 0 || depth <= maxDepth) {
            MavenConfig mvn = null;
            if (file != null && file.isDirectory() && file.exists() && new File(file, "pom.xml").exists()) {
                mvn = new MavenConfig(file.getAbsolutePath(), "", "");
                if (parent == null) {
                    result.add(mvn);
                } else {
                    parent.addChild(mvn);
                }
            }
            if (depth < maxDepth) {
                File[] files = file.listFiles();
                if (files != null) {
                    final MavenConfig nextParent = mvn;
                    Arrays.stream(files).filter(f -> f.isDirectory()).forEach(f -> scanMaven(f, result, depth + 1, maxDepth, nextParent));
                }
            }
        }
    }


}
